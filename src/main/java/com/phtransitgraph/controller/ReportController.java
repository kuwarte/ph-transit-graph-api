package com.phtransitgraph.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.phtransitgraph.dto.request.RejectRequest;
import com.phtransitgraph.dto.request.ReportRequest;
import com.phtransitgraph.dto.response.ReportResponse;
import com.phtransitgraph.service.ReportService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<ReportResponse> submitReport(
            @Valid @RequestBody ReportRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reportService.submitReport(req));
    }

    // temp
    @GetMapping("/my-reports")
    public ResponseEntity<List<ReportResponse>> getMyReports(
            @RequestParam String reporterId) {
        return ResponseEntity.ok(reportService.getMyReports(reporterId));
    }

    @GetMapping
    public ResponseEntity<List<ReportResponse>> getAllReports(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(reportService.getAllReports(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable String id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    // temp
    @PatchMapping("/{id}/approve")
    public ResponseEntity<ReportResponse> approveReport(
            @PathVariable String id,
            @RequestParam String reviewerId) {
        return ResponseEntity.ok(reportService.approveReport(id, reviewerId));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ReportResponse> rejectReport(
            @PathVariable String id,
            @RequestParam String reviewerId,
            @Valid @RequestBody RejectRequest req) {
        return ResponseEntity.ok(reportService.rejectReport(id, reviewerId, req));
    }
}
