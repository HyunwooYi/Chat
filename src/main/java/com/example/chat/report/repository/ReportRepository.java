package com.example.chat.report.repository;

import com.example.chat.report.entity.Report;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends ReactiveMongoRepository<Report, Long> {
}
