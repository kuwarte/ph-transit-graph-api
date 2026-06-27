package com.phtransitgraph.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SavedRouteResponse {
    private String id;
    private String userId;
    private String routeId;
    private String routeCode;
    private String routeName;
    private String originName;
    private String destinationName;
    private String vehicleType;
    private LocalDateTime savedAt;
}
