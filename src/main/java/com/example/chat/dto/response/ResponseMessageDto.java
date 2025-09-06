package com.example.chat.dto.response;

import com.example.chat.domain.message.document.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessageDto {

    private String id;        // ObjectId를 문자열로 변환해서 전달
    private Long roomId;      // 채팅방 ID
    private Long writerId;    // 작성자 ID
    private String writerName;  // 작성자 이름
    private String content;   // 메시지 내용
    private String createdAt; // 날짜

    public static ResponseMessageDto of (ChatMessage m) {
        return ResponseMessageDto.builder()
                .id(m.getId() != null ? m.getId().toHexString() : null)
                .roomId(m.getRoomId())
                .writerId(m.getWriterId())
                .writerName(null) // 이름 미포함 변환 시에는 null
                .content(m.getContent())
                .createdAt(m.getCreatedAt() != null ? m.getCreatedAt().toString() : null)
                .build();
    }
    /** ✅ 이름 포함 변환 */
    public static ResponseMessageDto of(ChatMessage m, String writerName) {
        return ResponseMessageDto.builder()
                .id(m.getId() != null ? m.getId().toHexString() : null)
                .roomId(m.getRoomId())
                .writerId(m.getWriterId())
                .writerName(writerName)
                .content(m.getContent())
                .createdAt(m.getCreatedAt() != null ? m.getCreatedAt().toString() : null)
                .build();
    }

}