package com.example.chat.admin.dto;

import com.example.chat.domain.message.document.ChatMessage;

public record AdminMessageDto (
    String id,
    Long roomId,
    Long writerId,
    String content,
    String createdAt
) {
    public static AdminMessageDto from(ChatMessage m) {
        return new AdminMessageDto(
                m.getId() != null ? m.getId().toHexString() : null,
                m.getRoomId(),
                m.getWriterId(),
                m.getContent(),
                m.getCreatedAt() != null ? m.getCreatedAt().toString() : null
        );
    }
}
