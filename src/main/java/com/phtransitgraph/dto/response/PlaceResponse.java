package com.phtransitgraph.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaceResponse {
    private String id;
    private String name;
    private String municipality;
    private String province;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
}
