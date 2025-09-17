package com.example.chat.admin.service;


import com.example.chat.domain.chatroom.entity.ChatRoom;
import com.example.chat.domain.chatroom.repository.ChatRoomRepository;
import com.example.chat.global.RestApiException;
import com.example.chat.global.errorcode.ChatRoomErrorCode;
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
                .orElseThrow(() -> new RestApiException(ChatRoomErrorCode.ROOM_NOT_FOUND));
    }

    public void delete(Long roomId) {
        if (!chatRoomRepository.existsById(roomId)) {
            throw new RestApiException(ChatRoomErrorCode.ROOM_NOT_FOUND);
        }
        chatRoomRepository.deleteById(roomId);
    }
}
