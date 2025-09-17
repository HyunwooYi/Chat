package com.example.chat.admin.service;

import com.example.chat.domain.message.document.ChatMessage;
import com.example.chat.domain.message.repository.ChatMessageRepository;
import com.example.chat.global.RestApiException;
import com.example.chat.global.errorcode.ChatRoomErrorCode;
import com.example.chat.global.errorcode.MemberErrorCode;
import com.example.chat.global.errorcode.MessageErrorCode;
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
        return chatMessageRepository.findById(id)
                .switchIfEmpty(Mono.error(new RestApiException(MessageErrorCode.RESOURCE_NOT_FOUND)));
    }

    public Mono<Void> deleteById(String id) {
        return chatMessageRepository.findById(id)
                .switchIfEmpty(Mono.error(new RestApiException(MessageErrorCode.RESOURCE_NOT_FOUND)))
                .flatMap(m -> chatMessageRepository.deleteById(id));

    }

    public Mono<Long> countByRoom(Long roomId) {
        return chatMessageRepository.countByRoomId(roomId);

    }
}
