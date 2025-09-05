package com.example.chat.domain.message.service;

import com.example.chat.domain.message.document.ChatMessage;
import com.example.chat.domain.message.repository.ChatMessageRepository;
import com.example.chat.dto.response.ResponseMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public Flux<ResponseMessageDto> findChatMessages(Long id) {
        return chatMessageRepository
                .findAllByRoomIdOrderByCreatedAtAsc(id)
                .map(ResponseMessageDto::of);
    }

    public Mono<ResponseMessageDto> saveMessage(Long roomId, Long writerId, String content) {
        ChatMessage entity = ChatMessage.builder()
                .roomId(roomId)
                .writerId(writerId)
                .content(content)
                .build();
        return chatMessageRepository.save(entity).map(ResponseMessageDto::of);
    }
}
