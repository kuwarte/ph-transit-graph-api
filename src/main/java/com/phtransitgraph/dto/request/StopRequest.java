package com.phtransitgraph.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StopRequest {

    @NotBlank(message = "stop name is required")
    private String stopName;

    @NotNull(message = "sequence order is required")
    @Min(value = 1, message = "sequence order must be at least 1")
    private Integer sequenceOrder;

    private Double latitude;
    private Double longitude;
    private String landmark;
}
