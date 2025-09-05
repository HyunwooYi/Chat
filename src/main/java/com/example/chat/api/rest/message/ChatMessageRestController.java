package com.example.chat.api.rest.message;

import com.example.chat.auth.PrincipalDetails;
import com.example.chat.domain.message.repository.ChatMessageRepository;
import com.example.chat.domain.message.service.ChatMessageService;
import com.example.chat.dto.response.ResponseMessageDto;
import com.example.chat.domain.message.document.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatMessageRestController {

    private final ChatMessageService chatMessageService;
    private final ChatMessageRepository chatMessageRepository;
    private final StringRedisTemplate stringRedisTemplate;


//    // 데모용: 임시 메시지 1건 반환 (블로킹 타입로 유지 가능)
//    @GetMapping("/chat/{id}")
//    public ResponseEntity<List<ChatMessage>> getChatMessages(@PathVariable Long id){
//        ChatMessage test = ChatMessage.builder()
//                .roomId(id)
//                .content("test")
//                .writerId(1L)
//                .build();
//        return ResponseEntity.ok().body(List.of(test));
//    }

    @GetMapping("/chat")
    public Mono<ResponseEntity<Object>> history(@RequestParam Long roomId,
                                                @AuthenticationPrincipal PrincipalDetails principal) {
        Long me = Long.valueOf(principal.getMember().getLoginId());
        String key = "room:" + roomId + ":members";
        Boolean ok = stringRedisTemplate.opsForSet().isMember(key, String.valueOf(me));
        if (!Boolean.TRUE.equals(ok)) throw new AccessDeniedException("not a room member");

        Flux<ResponseMessageDto> flux = chatMessageRepository.findAllByRoomIdOrderByCreatedAtAsc(roomId)
                .map(ResponseMessageDto::of);
        return flux.collectList().map(ResponseEntity::ok);
    }


//    // ✅ 리액티브 메시지 목록 → Mono<ResponseEntity<List<...>>> (collectList)
//    @GetMapping("/find/chat/list/{id}")
//    public Mono<ResponseEntity<List<ResponseMessageDto>>> findChatMessages(@PathVariable("id") Long id) {
//        return chatMessageService.findChatMessages(id)
//                .collectList()
//                .map(ResponseEntity::ok);
//    }
}