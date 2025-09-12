package com.example.chat.admin.service;


import com.example.chat.domain.chatroom.entity.ChatRoom;
import com.example.chat.domain.chatroom.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoom> findAll() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom get(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("room not found: " + roomId));
    }

    public void delete(Long roomId) {
        chatRoomRepository.deleteById(roomId);
    }
}
