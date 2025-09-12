package com.example.chat.dto.response;

import com.example.chat.domain.chatroom.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ResponseChatRoomDto {

    private Long id;
    private String title;
    private LocalDate localDate; // = ChatRoom.createdDate

    public static ResponseChatRoomDto from(ChatRoom chatRoom) {
        return new ResponseChatRoomDto(
                chatRoom.getId(),
                chatRoom.getTitle(),
                chatRoom.getCreatedDate()
        );
    }
}