package com.example.chat.api.messaging;

import com.example.chat.domain.message.service.ChatMessageService;
import com.example.chat.dto.request.SendMessagePayload;
import com.example.chat.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import java.security.Principal;
import org.springframework.stereotype.Controller;

// STOMP 컨드롤러 분리
@Controller
@RequiredArgsConstructor
public class ChatStompController {

    private final SimpMessageSendingOperations template;
    private final ChatMessageService chatMessageService;

    // STOMP 메시지 핸들러: HTTP가 아니므로 ResponseEntity 반환 불필요
    @MessageMapping("/chat/{roomId}")   //  /pub/chat/{roomId}
    public void handleSend(@DestinationVariable("roomId") Long roomId,
                           @Payload SendMessagePayload payload,
                           Principal principal) {
        Long writerId = SecurityUtils.getMemberId(principal);
        chatMessageService.saveMessage(roomId, writerId, payload.getContent())
                .doOnNext(savedDto ->
                        template.convertAndSend("/sub/chatroom/" + roomId, savedDto)
                )
                .subscribe();
    }
}
