package com.phtransitgraph.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReportRequest {

    // temp
    @NotBlank(message = "reporterId is required")
    private String reporterId;

    private String routeId;
    private String stopId;
    private String fareId;

    @NotBlank(message = "reportType is required")
    private String reportType;

    @NotBlank(message = "description is required")
    private String description;
}
