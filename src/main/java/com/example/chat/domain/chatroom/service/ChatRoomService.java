package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.entity.ChatRoom;
import com.example.chat.domain.chatroom.repository.ChatRoomRepository;
import com.example.chat.dto.request.RequestChatRoomDto;
import com.example.chat.dto.response.ResponseChatRoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final StringRedisTemplate redis;


    public ResponseChatRoomDto createChatRoom(RequestChatRoomDto dto) {
        // ChatRoom 생성자에서 createdDate는 LocalDate.now()로 자동 세팅되도록 해두세요.
        ChatRoom saved = chatRoomRepository.save(new ChatRoom(dto.getTitle()));
        return ResponseChatRoomDto.of(saved);
    }

    public void joinRoom(Long roomId, Long memberId) {
        String key = "room:" + roomId + ":members";
        redis.opsForSet().add(key, String.valueOf(memberId));
    }

    @Transactional(readOnly = true)
    public List<ResponseChatRoomDto> findChatRoomList() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        return chatRooms.stream().map(ResponseChatRoomDto::of).collect(Collectors.toList());
    }
}
