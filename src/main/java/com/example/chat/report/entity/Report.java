package com.example.chat.report.entity;

import com.example.chat.report.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("reports")
@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    private String id;

    private String messageId;
    private Long reporterId;
    private Long reportedMemberId;

    private ReportReason reason;
    private String detail;

    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
