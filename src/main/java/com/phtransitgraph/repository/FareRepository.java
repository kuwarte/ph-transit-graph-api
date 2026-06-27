package com.phtransitgraph.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.phtransitgraph.entity.Fare;

public interface FareRepository extends JpaRepository<Fare, String> {
    List<Fare> findByRouteId(String routeId);

    Optional<Fare> findByIdAndRouteId(String id, String routeId);

    Optional<Fare> findTopByFromStopIdAndToStopIdOrderByEffectiveDateDesc(
            String fromStopId, String toStopId);

    boolean existsByRouteIdAndFromStopIdAndToStopIdAndEffectiveDate(
            String routeId, String fromStopId, String toStopId, LocalDate effectiveDate);
}
