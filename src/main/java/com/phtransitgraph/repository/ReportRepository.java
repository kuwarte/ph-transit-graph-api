package com.phtransitgraph.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.phtransitgraph.entity.Report;
import com.phtransitgraph.enums.ReportStatus;

public interface ReportRepository extends JpaRepository<Report, String> {
    List<Report> findByReporterId(String reporterId);

    List<Report> findByStatus(ReportStatus status);

    List<Report> findByReporterIdOrderByCreatedAtDesc(String reporterId);
}
