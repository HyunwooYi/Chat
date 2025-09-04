package com.example.chat.api.messaging;

import com.example.chat.domain.message.document.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

// STOMP 컨드롤러 분리
@Controller
@RequiredArgsConstructor
public class ChatStompController {

    private final SimpMessageSendingOperations template;

    // STOMP 메시지 핸들러: HTTP가 아니므로 ResponseEntity 반환 불필요
    @MessageMapping("/message")
    public void receiveMessage(ChatMessage chat) {
        Long roomId = chat.getRoomId() != null ? chat.getRoomId() : 1L;
        template.convertAndSend("/sub/chatroom/" + roomId, chat);
    }
}
