package com.example.chat.domain.message.service;

import com.example.chat.domain.message.document.ChatMessage;
import com.example.chat.domain.message.repository.ChatMessageRepository;
import com.example.chat.dto.response.ResponseMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public Flux<ResponseMessageDto> findChatMessages(Long id) {
        Flux<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomIdOrderByLocalDateAsc(id);
        return chatMessages.map(ResponseMessageDto::of);
    }
}
