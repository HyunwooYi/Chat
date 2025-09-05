package com.example.chat.websocket;


import com.example.chat.auth.PrincipalDetails;
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

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class MembershipChannelInterceptor implements ChannelInterceptor {

    private final StringRedisTemplate redis;
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
        Object principal = auth.getPrincipal(); // <- 여기!
        Long memberId = resolveMemberId(principal); // 아래 헬퍼

        if (cmd == StompCommand.SUBSCRIBE) {
            String dest = acc.getDestination();
            if (dest != null && dest.startsWith(SUB_PREFIX)) {
                Long roomId = parseRoomId(dest, SUB_PREFIX);
                ensureMember(roomId, memberId);
            }
        } else if (cmd == StompCommand.SEND) {
            String dest = acc.getDestination();
            if (dest != null && dest.startsWith(PUB_PREFIX)) {
                Long roomId = parseRoomId(dest, PUB_PREFIX);
                ensureMember(roomId, memberId);
            }
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
}