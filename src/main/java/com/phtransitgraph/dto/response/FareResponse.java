package com.phtransitgraph.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FareResponse {
    private String id;
    private String routeId;
    private String fromStopId;
    private String fromStopName;
    private String toStopId;
    private String toStopName;
    private BigDecimal baseFare;
    private BigDecimal discountedFare;
    private LocalDate effectiveDate;
    private LocalDateTime createdAt;
}
