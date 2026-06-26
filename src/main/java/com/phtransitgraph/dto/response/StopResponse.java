package com.phtransitgraph.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StopResponse {
    private String id;
    private String routeId;
    private String stopName;
    private Integer sequenceOrder;
    private Double latitude;
    private Double longitude;
    private String landmark;
    private LocalDateTime createdAt;
}
