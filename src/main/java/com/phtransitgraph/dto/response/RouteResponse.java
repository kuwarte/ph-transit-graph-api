package com.phtransitgraph.dto.response;

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

    public RouteResponse(String id, String routeCode, String routeName,
            String originName, String originMunicipality,
            String destinationName, String destinationMunicipality,
            String vehicleType, String status) {
        this.id = id;
        this.routeCode = routeCode;
        this.routeName = routeName;
        this.originName = originName;
        this.originMunicipality = originMunicipality;
        this.destinationName = destinationName;
        this.destinationMunicipality = destinationMunicipality;
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

    public String getOriginName() {
        return originName;
    }

    public String getOriginMunicipality() {
        return originMunicipality;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public String getDestinationMunicipality() {
        return destinationMunicipality;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getStatus() {
        return status;
    }
}
