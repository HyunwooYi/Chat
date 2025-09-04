package com.example.chat.dto.response;

import com.example.chat.domain.message.document.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseMessageDto {

    private String id;        // ObjectId를 문자열로 변환해서 전달
    private Long roomId;      // 채팅방 ID
    private Long writerId;    // 작성자 ID
    private String content;   // 메시지 내용
    private String localDate; // 날짜 (문자열로 내려줌)

    // Entity -> DTO 변환 메서드
    public static ResponseMessageDto fromEntity(ChatMessage message) {
        return ResponseMessageDto.builder()
                .id(message.getId() != null ? message.getId().toHexString() : null)
                .roomId(message.getRoomId())
                .writerId(message.getWriterId())
                .content(message.getContent())
                .localDate(message.getLocalDate() != null ? message.getLocalDate().toString() : null)
                .build();
    }

    // ★ 별칭: of() 추가 (ResponseChatRoomDto와 일관성)
    public static ResponseMessageDto of(ChatMessage message) {
        return fromEntity(message);
    }
}