package com.phtransitgraph.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.phtransitgraph.entity.SavedRoute;

public interface SavedRouteRepository extends JpaRepository<SavedRoute, String> {
    List<SavedRoute> findByUserId(String userId);

    Optional<SavedRoute> findByUserIdAndRouteId(String userId, String routeId);

    boolean existsByUserIdAndRouteId(String userId, String routeId);
}
