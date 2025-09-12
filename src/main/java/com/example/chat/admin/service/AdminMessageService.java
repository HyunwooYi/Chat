package com.example.chat.admin.service;

import com.example.chat.domain.message.document.ChatMessage;
import com.example.chat.domain.message.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AdminMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public Flux<ChatMessage> findByRoomDesc(Long roomId) {
        return chatMessageRepository.findAllByRoomIdOrderByCreatedAtDesc(roomId);
    }

    public Mono<ChatMessage> getById(String id) {
        return chatMessageRepository.findById(id);
    }

    public Mono<Void> deleteById(String id) {
        return chatMessageRepository.deleteById(id);
    }

    public Mono<Void> purgeByRoom(Long roomId) {
        return chatMessageRepository.deleteByRoomId(roomId);
    }

    public Mono<Long> countByRoom(Long roomId) {
        return chatMessageRepository.countByRoomId(roomId);

    }
}
