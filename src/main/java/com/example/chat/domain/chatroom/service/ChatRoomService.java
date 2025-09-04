package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.entity.ChatRoom;
import com.example.chat.domain.chatroom.repository.ChatRoomRepository;
import com.example.chat.dto.request.RequestChatRoomDto;
import com.example.chat.dto.response.ResponseChatRoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ResponseChatRoomDto createChatRoom(RequestChatRoomDto requestChatRoomDto) {
        ChatRoom chatRoom = new ChatRoom(requestChatRoomDto.getTitle(), LocalDate.now());
        return ResponseChatRoomDto.of(chatRoomRepository.save(chatRoom));
    }

    @Transactional(readOnly = true)
    public List<ResponseChatRoomDto> findChatRoomList() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        return chatRooms.stream().map(ResponseChatRoomDto::of).collect(Collectors.toList());
    }
}
