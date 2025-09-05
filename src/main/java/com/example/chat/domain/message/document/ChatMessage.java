package com.example.chat.domain.message.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "Chat-Collection") // 실제 몽고 DB 컬렉션 이름
@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class ChatMessage {

    @Id
    private ObjectId id;

    @Indexed
    private Long roomId;

    private String content;
    private Long writerId;

    @CreatedDate
    @Indexed
    private Instant createdAt;  // @CreatedDate가 자동으로 넣어줌

    public ChatMessage(Long roomId, String content, Long writerId) {
        this.roomId = roomId;
        this.content = content;
        this.writerId = writerId;
    }
}