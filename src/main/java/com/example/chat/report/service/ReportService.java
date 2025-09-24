package com.example.chat.report.service;

import com.example.chat.admin.service.AdminMessageService;
import com.example.chat.global.RestApiException;
import com.example.chat.global.errorcode.MessageErrorCode;
import com.example.chat.report.dto.ReportCreateRequest;
import com.example.chat.report.entity.Report;
import com.example.chat.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final AdminMessageService adminMessageService;

    @Transactional
    public Mono<String> createReport(Long reporterId, ReportCreateRequest req) {
        return adminMessageService.getById(req.messageId()) // req.messageId(): String(ObjectId hex)
                .switchIfEmpty(Mono.error(new RestApiException(MessageErrorCode.RESOURCE_NOT_FOUND)))
                .flatMap(msg -> {
                    // msg.getId(): ObjectId → hex 문자열로 변환 필요
                    final String messageHexId = msg.getId().toHexString();

                    Report report = Report.builder()
                            .messageId(messageHexId)           // 메시지 ID 저장
                            .reporterId(reporterId)            // 신고자
                            .reportedMemberId(msg.getWriterId()) // 신고 당한 사람
                            .reason(req.reason())
                            .detail(req.detail())
                            .build();

                    return reportRepository.save(report).map(Report::getId); // 저장된 Report의 String id 반환
                });
    }

    public Flux<Report> getReports() {
        return reportRepository.findAll();
    }
}
