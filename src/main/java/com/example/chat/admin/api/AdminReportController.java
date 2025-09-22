package com.example.chat.admin.api;

import com.example.chat.report.entity.Report;
import com.example.chat.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/reports")
public class AdminReportController {
    private final ReportService reportService;

    // 신고된 목록 조회
    @GetMapping("")
    public Flux<Report> getReports() {
        return reportService.getReports();
    }

}
