package com.example.chat.domain.chatroom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity @Table(name = "chat_room")
@Getter @NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    public ChatRoom(String title) {
        this.title = title;
        this.createdDate = LocalDate.now();
    }

}


