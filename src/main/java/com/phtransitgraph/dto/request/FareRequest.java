package com.phtransitgraph.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FareRequest {
    @NotBlank(message = "from stop id is required")
    private String fromStopId;

    @NotBlank(message = "to stop id is required")
    private String toStopId;

    @NotNull(message = "base fare is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "base fare must be greater than 0")
    private BigDecimal baseFare;

    private BigDecimal discountedFare;

    @NotNull(message = "effective date is required")
    private LocalDate effectiveDate;
}
