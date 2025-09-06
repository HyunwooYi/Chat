package com.example.chat.domain.message.service;

import com.example.chat.domain.message.document.ChatMessage;
import com.example.chat.domain.message.repository.ChatMessageRepository;
import com.example.chat.dto.response.ResponseMessageDto;
import com.example.chat.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.example.chat.member.entity.Member;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;

    public Flux<ResponseMessageDto> findChatMessages(Long roomId) {
        return chatMessageRepository.findAllByRoomIdOrderByCreatedAtAsc(roomId)
                .map(m -> {
                    String username = memberRepository.findById(m.getWriterId())
                            .map(Member::getUsername)
                            .orElse("알수없음");
                    return ResponseMessageDto.of(m, username);
                });
    }

    public Mono<ResponseMessageDto> saveMessage(Long roomId, Long writerId, String content) {
        ChatMessage message = new ChatMessage(roomId, content, writerId);
        return chatMessageRepository.save(message)
                .map(saved -> {
                    // 👇 writerId -> username 조회
                    String username = memberRepository.findById(writerId)
                            .map(Member::getUsername)
                            .orElse("알수없음");
                    return ResponseMessageDto.of(saved, username);
                });
    }
}
