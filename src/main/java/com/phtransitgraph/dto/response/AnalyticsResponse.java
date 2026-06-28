package com.phtransitgraph.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalyticsResponse {

    private long totalUsers;
    private long totalOperators;
    private long totalRoutes;
    private long totalReports;

}
