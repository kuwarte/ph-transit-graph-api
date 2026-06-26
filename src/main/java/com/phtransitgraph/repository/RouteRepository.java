package com.phtransitgraph.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.phtransitgraph.entity.Route;
import com.phtransitgraph.enums.VehicleType;

@Repository
public interface RouteRepository extends JpaRepository<Route, String> {
    Optional<Route> findByRouteCode(String routeCode);

    List<Route> findByVehicleType(VehicleType vehicleType);

    @Query("SELECT r FROM Route r WHERE " +
            "LOWER(r.origin.name) LIKE LOWER(CONCAT('%', :origin, '%')) AND " +
            "LOWER(r.destination.name) LIKE LOWER(CONCAT('%', :destination, '%'))")
    List<Route> searchByOriginAndDestination(
            @Param("origin") String origin,
            @Param("destination") String destination);
}
