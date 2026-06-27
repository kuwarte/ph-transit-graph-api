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
import com.phtransitgraph.repository.UserRepository;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final RouteRepository routeRepository;
    private final StopRepository stopRepository;
    private final FareRepository fareRepository;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository,
            RouteRepository routeRepository, StopRepository stopRepository,
            FareRepository fareRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.routeRepository = routeRepository;
        this.stopRepository = stopRepository;
        this.fareRepository = fareRepository;
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

    private User findUserOrThrow(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
    }

    public ReportResponse submitReport(ReportRequest req) {
        User reporter = findUserOrThrow(req.getReporterId());

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

    public List<ReportResponse> getMyReports(String reporterId) {
        findUserOrThrow(reporterId);
        return reportRepository.findByReporterIdOrderByCreatedAtDesc(reporterId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ReportResponse> getAllReports(String status) {
        if (status != null) {
            ReportStatus reportStatus = ReportStatus.valueOf(status.toUpperCase());
            return reportRepository.findByStatus(reportStatus)
                    .stream()
                    .map(this::toResponse)
                    .toList();
        }
        return reportRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ReportResponse getReportById(String id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Report not found with id: " + id));
        return toResponse(report);
    }

    public ReportResponse approveReport(String id, String reviewerId) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Report not found with id: " + id));
        User reviewer = findUserOrThrow(reviewerId);

        // temp
        report.setStatus(ReportStatus.APPROVED);
        report.setReviewedBy(reviewer);
        report.setReviewedAt(LocalDateTime.now());

        return toResponse(reportRepository.save(report));
    }

    public ReportResponse rejectReport(String id, String reviewerId, RejectRequest req) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Report not found with id: " + id));
        User reviewer = findUserOrThrow(reviewerId);

        report.setStatus(ReportStatus.REJECTED);
        report.setReviewedBy(reviewer);
        report.setReviewedAt(LocalDateTime.now());
        report.setRejectionReason(req.getReason());

        return toResponse(reportRepository.save(report));
    }
}
