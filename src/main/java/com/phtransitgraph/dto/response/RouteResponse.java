package com.phtransitgraph.dto.response;

public class RouteResponse {
    private String id;
    private String routeCode;
    private String routeName;
    private String origin;
    private String destination;
    private String vehicleType;
    private String status;

    public RouteResponse(String id, String routeCode, String routeName, String origin, String destination,
            String vehicleType, String status) {
        this.id = id;
        this.routeCode = routeCode;
        this.routeName = routeName;
        this.origin = origin;
        this.destination = destination;
        this.vehicleType = vehicleType;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public String getRouteName() {
        return routeName;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getStatus() {
        return status;
    }

}
