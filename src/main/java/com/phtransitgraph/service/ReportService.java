package com.phtransitgraph.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.phtransitgraph.dto.request.RejectRequest;
import com.phtransitgraph.dto.request.ReportRequest;
import com.phtransitgraph.dto.response.ReportResponse;
import com.phtransitgraph.entity.Fare;
import com.phtransitgraph.entity.Report;
import com.phtransitgraph.entity.Route;
import com.phtransitgraph.entity.Stop;
import com.phtransitgraph.entity.User;
import com.phtransitgraph.enums.ReportStatus;
import com.phtransitgraph.enums.ReportType;
import com.phtransitgraph.exception.ResourceNotFoundException;
import com.phtransitgraph.repository.FareRepository;
import com.phtransitgraph.repository.ReportRepository;
import com.phtransitgraph.repository.RouteRepository;
import com.phtransitgraph.repository.StopRepository;
import com.phtransitgraph.security.OwnershipValidator;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final RouteRepository routeRepository;
    private final StopRepository stopRepository;
    private final FareRepository fareRepository;
    private final OwnershipValidator ownershipValidator;

    public ReportService(ReportRepository reportRepository, RouteRepository routeRepository,
            StopRepository stopRepository, FareRepository fareRepository,
            OwnershipValidator ownershipValidator) {
        this.reportRepository = reportRepository;
        this.routeRepository = routeRepository;
        this.stopRepository = stopRepository;
        this.fareRepository = fareRepository;
        this.ownershipValidator = ownershipValidator;
    }

    private ReportResponse toResponse(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getReporter().getId(),
                report.getReporter().getFullName(),
                report.getRoute() != null ? report.getRoute().getId() : null,
                report.getStop() != null ? report.getStop().getId() : null,
                report.getFare() != null ? report.getFare().getId() : null,
                report.getReportType().name(),
                report.getDescription(),
                report.getStatus().name(),
                report.getReviewedBy() != null ? report.getReviewedBy().getId() : null,
                report.getReviewedBy() != null ? report.getReviewedBy().getFullName() : null,
                report.getReviewedAt(),
                report.getRejectionReason(),
                report.getCreatedAt());
    }

    public ReportResponse submitReport(ReportRequest req, String email) {
        User reporter = ownershipValidator.getUserFromEmail(email);

        Route route = null;
        if (req.getRouteId() != null) {
            route = routeRepository.findById(req.getRouteId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Route not found with id: " + req.getRouteId()));
        }
        Stop stop = null;
        if (req.getStopId() != null) {
            stop = stopRepository.findById(req.getStopId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Stop not found with id: " + req.getStopId()));
        }
        Fare fare = null;
        if (req.getFareId() != null) {
            fare = fareRepository.findById(req.getFareId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Fare not found with id: " + req.getFareId()));
        }
        Report report = Report.builder()
                .reporter(reporter)
                .route(route)
                .stop(stop)
                .fare(fare)
                .reportType(ReportType.valueOf(req.getReportType().toUpperCase()))
                .description(req.getDescription())
                .build();
        return toResponse(reportRepository.save(report));
    }

    public List<ReportResponse> getMyReports(String email) {
        User user = ownershipValidator.getUserFromEmail(email);
        return reportRepository.findByReporterIdOrderByCreatedAtDesc(user.getId())
                .stream().map(this::toResponse).toList();
    }

    public List<ReportResponse> getAllReports(String status) {
        if (status != null) {
            ReportStatus reportStatus = ReportStatus.valueOf(status.toUpperCase());
            return reportRepository.findByStatus(reportStatus)
                    .stream().map(this::toResponse).toList();
        }
        return reportRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ReportResponse getReportById(String id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Report not found with id: " + id));
        return toResponse(report);
    }

    public ReportResponse approveReport(String id, String reviewerEmail) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Report not found with id: " + id));
        User reviewer = ownershipValidator.getUserFromEmail(reviewerEmail);
        report.setStatus(ReportStatus.APPROVED);
        report.setReviewedBy(reviewer);
        report.setReviewedAt(LocalDateTime.now());
        return toResponse(reportRepository.save(report));
    }

    public ReportResponse rejectReport(String id, String reviewerEmail, RejectRequest req) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Report not found with id: " + id));
        User reviewer = ownershipValidator.getUserFromEmail(reviewerEmail);
        report.setStatus(ReportStatus.REJECTED);
        report.setReviewedBy(reviewer);
        report.setReviewedAt(LocalDateTime.now());
        report.setRejectionReason(req.getReason());
        return toResponse(reportRepository.save(report));
    }
}
