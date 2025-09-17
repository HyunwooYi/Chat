package com.example.chat.websocket;


import com.example.chat.auth.PrincipalDetails;
import com.example.chat.member.entity.MemberStatus;
import com.example.chat.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MembershipChannelInterceptor implements ChannelInterceptor {

    private final StringRedisTemplate redis;
    private final MemberRepository memberRepository;
    private static final String SUB_PREFIX = "/sub/chatroom/"; // 기존 구독 경로
    private static final String PUB_PREFIX = "/pub/chat/";     // 발행 경로(컨트롤러 @MessageMapping와 매칭)

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor acc = StompHeaderAccessor.wrap(message);
        StompCommand cmd = acc.getCommand();
        if (cmd == null) return message;

        // CONNECT / DISCONNECT / HEARTBEAT 같은 초기 프레임은 검증하지 않음
        if (!(cmd == StompCommand.SUBSCRIBE || cmd == StompCommand.SEND)) {
            return message;
        }

        Authentication auth = (Authentication) acc.getUser();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("unauthenticated");
        }

        // 1. 관리자 우회: ADMIN이면 멤버십 검증 생략
        if (isAdmin(auth)) {
            return message; // 바로 통과
        }

        // 밴 유저 차단 (SUBSCRIBE/SEND에서만)
        if (isBanned(auth)) {
            throw new AccessDeniedException("banned user");
        }


        // 멤버십 검증 (ROOM 가입자만 SUB/SEND 가능)
        final Object principal = auth.getPrincipal();
        final Long memberId = resolveMemberId(principal);

        final String dest = acc.getDestination();
        if (dest == null) return message;

        if (cmd == StompCommand.SUBSCRIBE && dest.startsWith(SUB_PREFIX)) {
            Long roomId = parseRoomId(dest, SUB_PREFIX);
            ensureMember(roomId, memberId);
        } else if (cmd == StompCommand.SEND && dest.startsWith(PUB_PREFIX)) {
            Long roomId = parseRoomId(dest, PUB_PREFIX);
            ensureMember(roomId, memberId);
        }
        return message;
    }

    private void ensureMember(Long roomId, Long memberId) {
        String key = "room:" + roomId + ":members";
        Boolean ok = redis.opsForSet().isMember(key, String.valueOf(memberId));
        if (!Boolean.TRUE.equals(ok)) {
            throw new AccessDeniedException("not a room member");
        }
    }

    private Long parseRoomId(String dest, String prefix) {
        String id = dest.substring(prefix.length());
        int slash = id.indexOf('/');
        if (slash >= 0) id = id.substring(0, slash);
        return Long.valueOf(id);
    }

    private Long resolveMemberId(Object principal) {
        if (principal instanceof PrincipalDetails pd) {
            return pd.getMember().getMemberId();
        }
        if (principal instanceof OAuth2User ou) {
            // 혹시 다른 구현체로 올 때 대비 (필요 시 커스터마이즈)
            Object loginId = ou.getAttributes().get("loginId");
            if (loginId != null) return Long.valueOf(String.valueOf(loginId));
        }
        throw new AccessDeniedException("unsupported principal");
    }

    private boolean isAdmin(Authentication auth) {
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private boolean isBanned(Authentication auth) {
        Long memberId = resolveMemberId(auth.getPrincipal());
        return memberRepository.findById(memberId)
                .map(m -> m.getMemberStatus() == MemberStatus.BANNED)
                .orElse(false);
    }
}