package com.phtransitgraph.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phtransitgraph.entity.Route;
import com.phtransitgraph.enums.VehicleType;

@Repository
public interface RouteRepository extends JpaRepository<Route, String> {
    Optional<Route> findByRouteCode(String routeCode);

    List<Route> findByVehicleType(VehicleType vehicleType);

    List<Route> findByOriginContainingIgnoreCaseAndDestinationContainingIgnoreCase(String origin, String destination);

}
