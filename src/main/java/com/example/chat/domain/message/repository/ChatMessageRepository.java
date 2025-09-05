package com.example.chat.domain.message.repository;

import com.example.chat.domain.message.document.ChatMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String> {
    Flux<ChatMessage> findAllByRoomIdOrderByCreatedAtAsc(Long roomId);
}
