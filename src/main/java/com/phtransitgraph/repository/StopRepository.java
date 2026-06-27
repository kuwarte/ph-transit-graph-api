package com.phtransitgraph.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phtransitgraph.entity.Stop;

public interface StopRepository extends JpaRepository<Stop, String> {
    List<Stop> findByRouteIdOrderBySequenceOrderAsc(String routeId);

    Optional<Stop> findByIdAndRouteId(String id, String routeId);

    boolean existsByRouteIdAndSequenceOrder(String routeId, Integer sequenceOrder);

}
