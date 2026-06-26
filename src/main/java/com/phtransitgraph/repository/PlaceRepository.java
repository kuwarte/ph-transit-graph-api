package com.phtransitgraph.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phtransitgraph.entity.Place;

public interface PlaceRepository extends JpaRepository<Place, String> {

    List<Place> findByMunicipalityIgnoreCase(String municipality);

    List<Place> findByProvinceIgnoreCase(String province);

    Optional<Place> findByNameIgnoreCaseAndMunicipalityIgnoreCase(
            String name, String municipality);

}
