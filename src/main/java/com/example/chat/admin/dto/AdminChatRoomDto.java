package com.example.chat.admin.dto;

import com.example.chat.domain.chatroom.entity.ChatRoom;

public record AdminChatRoomDto(Long id, String title, String createdDate) {
    public static AdminChatRoomDto from(ChatRoom r) {
        return new AdminChatRoomDto(r.getId(), r.getTitle(),
                r.getCreatedDate() != null ? r.getCreatedDate().toString() : null);
    }
}
