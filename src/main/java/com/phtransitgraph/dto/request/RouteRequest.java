package com.phtransitgraph.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RouteRequest {

    @NotBlank(message = "routeCode is required")
    private String routeCode;

    @NotBlank(message = "routeName is required")
    private String routeName;

    @NotNull(message = "originId is required")
    private String originId;

    @NotNull(message = "destinationId is required")
    private String destinationId;

    @NotBlank(message = "vehicleType is required")
    private String vehicleType;
}
