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
    private String latitude;
    private String longitude;
    private LocalDateTime createdAt;
}
