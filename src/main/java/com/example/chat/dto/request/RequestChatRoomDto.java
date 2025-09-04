package com.example.chat.dto.request;

import com.example.chat.domain.chatroom.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class RequestChatRoomDto {

    private Long id;
    private String title;
    private LocalDate localDate;

    public static RequestChatRoomDto of(ChatRoom chatRoom) {
        return new RequestChatRoomDto(chatRoom.getId(), chatRoom.getTitle(),
                chatRoom.getCreatedDate());
    }
}
