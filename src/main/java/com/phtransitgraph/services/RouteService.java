package com.phtransitgraph.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.phtransitgraph.dto.response.RouteResponse;

@Service
public class RouteService {

    public List<RouteResponse> getAllRoutes() {
        return Arrays.asList(
                new RouteResponse("1", "GEN-TRIAS-CUBAO",
                        "General Trias - Cubao",
                        "General Trias", "Cubao", "JEEPNEY", "ACTIVE"),

                new RouteResponse("2", "IMUS-BACLARAN",
                        "Imus - Baclaran",
                        "Imus", "Baclaran", "JEEPNEY", "ACTIVE"),

                new RouteResponse("3", "DASMA-LAWTON",
                        "Dasmariñas - Lawton",
                        "Dasmariñas", "Lawton", "BUS", "ACTIVE"));
    }
}
