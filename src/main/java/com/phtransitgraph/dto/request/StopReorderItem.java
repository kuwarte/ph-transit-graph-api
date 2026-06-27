package com.phtransitgraph.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StopReorderItem {

    @NotBlank(message = "stop id is required")
    private String stopId;

    @NotNull(message = "sequence order is required")
    @Min(value = 1, message = "sequence order must be at least 1")
    private Integer sequenceOrder;
}
