package com.example.chat.api.rest.message;

import com.example.chat.domain.message.service.ChatMessageService;
import com.example.chat.dto.response.ResponseMessageDto;
import com.example.chat.domain.message.document.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatMessageRestController {

    private final ChatMessageService chatMessageService;

    // 데모용: 임시 메시지 1건 반환 (블로킹 타입로 유지 가능)
    @GetMapping("/chat/{id}")
    public ResponseEntity<List<ChatMessage>> getChatMessages(@PathVariable Long id){
        ChatMessage test = ChatMessage.builder()
                .roomId(id)
                .content("test")
                .writerId(1L)
                .localDate(LocalDate.now())
                .build();
        return ResponseEntity.ok().body(List.of(test));
    }


    // ✅ 리액티브 메시지 목록 → Mono<ResponseEntity<List<...>>> (collectList)
    @GetMapping("/find/chat/list/{id}")
    public Mono<ResponseEntity<List<ResponseMessageDto>>> findChatMessages(@PathVariable("id") Long id) {
        return chatMessageService.findChatMessages(id)
                .collectList()
                .map(ResponseEntity::ok);
    }
}