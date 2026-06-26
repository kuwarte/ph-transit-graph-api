package com.phtransitgraph.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PlaceRequest {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "municipality is required")
    private String municipality;

    @NotBlank(message = "province is required")
    private String province;

    private Double latitude;
    private Double longitude;
}
