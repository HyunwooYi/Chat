package com.example.chat.domain.message.document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "Chat-Collection") // 실제 몽고 DB 컬렉션 이름
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    private ObjectId id;
    private Long roomId;
    private String content;
    private Long writerId;
    private LocalDate localDate;

    public ChatMessage(Long roomId, String content, Long writerId, LocalDate localDate) {
        this.roomId = roomId;
        this.content = content;
        this.writerId = writerId;
        this.localDate = localDate;
    }
}