package com.phtransitgraph.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.phtransitgraph.dto.request.RejectRequest;
import com.phtransitgraph.dto.request.ReportRequest;
import com.phtransitgraph.dto.response.ReportResponse;
import com.phtransitgraph.service.ReportService;
import jakarta.validation.Valid;

@Tag(name = "Reports", description = "Submit and review community data correction reports")
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "Submit a report flagging incorrect data")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReportResponse> submitReport(
            @Valid @RequestBody ReportRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reportService.submitReport(req, userDetails.getUsername()));
    }

    @Operation(summary = "Get all reports submitted by the authenticated user")
    @GetMapping("/my-reports")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReportResponse>> getMyReports(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(reportService.getMyReports(userDetails.getUsername()));
    }

    @Operation(summary = "Get all reports, optionally filtered by status (admin only)")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReportResponse>> getAllReports(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(reportService.getAllReports(status));
    }

    @Operation(summary = "Get a single report by ID (admin only)")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable String id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @Operation(summary = "Approve a report (admin only)")
    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportResponse> approveReport(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                reportService.approveReport(id, userDetails.getUsername()));
    }

    @Operation(summary = "Reject a report with a reason (admin only)")
    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportResponse> rejectReport(
            @PathVariable String id,
            @Valid @RequestBody RejectRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                reportService.rejectReport(id, userDetails.getUsername(), req));
    }
}
