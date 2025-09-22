package com.example.chat.report.controller;

import com.example.chat.auth.PrincipalDetails;
import com.example.chat.report.dto.ReportCreateRequest;
import com.example.chat.report.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("")
    public Mono<ResponseEntity<Map<String, Object>>> createReport(
            @AuthenticationPrincipal PrincipalDetails principal,
            @Valid @RequestBody ReportCreateRequest request) {

        Long reporterId = principal.getMember().getMemberId();
        return reportService.createReport(reporterId, request)
                .map(id -> ResponseEntity
                        .created(URI.create("/reports/" + id))
                        .body(Map.of("id", id, "message", "신고가 접수되었습니다")));
    }

}
