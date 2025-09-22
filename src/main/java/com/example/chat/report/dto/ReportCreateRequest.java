package com.example.chat.report.dto;

import com.example.chat.report.ReportReason;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReportCreateRequest(
    @NotBlank String messageId,
    @NotNull ReportReason reason,
    @Size(max = 100) String detail
){}
