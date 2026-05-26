package com.naveenmandal.TravelWith.repository;

import com.naveenmandal.TravelWith.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepo extends JpaRepository<Station, Long> {

    // Search anywhere in the station name so entries like "NEW DELHI" appear for "delhi".
    List<Station> findTop20ByNameContainingIgnoreCaseOrderByNameAsc(String query);

    boolean existsByName(String name);
}
