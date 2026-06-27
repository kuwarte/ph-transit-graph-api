package com.phtransitgraph.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteResponse {
    private String id;
    private String routeCode;
    private String routeName;
    private String originName;
    private String originMunicipality;
    private String destinationName;
    private String destinationMunicipality;
    private String vehicleType;
    private String status;
    private String operatorId;
    private String operatorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
