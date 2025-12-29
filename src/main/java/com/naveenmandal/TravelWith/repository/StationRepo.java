package com.naveenmandal.TravelWith.repository;

import com.naveenmandal.TravelWith.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepo extends JpaRepository<Station, Long> {

    // Prefix-only search (autocomplete)
    List<Station> findTop20ByNameStartingWithOrderByNameAsc(String prefix);

    boolean existsByName(String name);
}
