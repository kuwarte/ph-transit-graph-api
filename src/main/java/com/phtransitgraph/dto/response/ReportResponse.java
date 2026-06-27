package com.phtransitgraph.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportResponse {
    private String id;
    private String reporterId;
    private String reporterName;
    private String routeId;
    private String stopId;
    private String fareId;
    private String reportType;
    private String description;
    private String status;
    private String reviewedById;
    private String reviewedByName;
    private LocalDateTime reviewedAt;
    private String rejectionReason;
    private LocalDateTime createdAt;
}
