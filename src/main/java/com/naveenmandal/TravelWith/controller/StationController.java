package com.naveenmandal.TravelWith.controller;

import com.naveenmandal.TravelWith.repository.StationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationController {

    private final StationRepo stationRepo;

    @GetMapping
    public List<String> autocomplete(@RequestParam("q") String q) {
        q = (q == null) ? "" : q.trim();
        if (q.length() < 2) return List.of();

        return stationRepo.findTop20ByNameStartingWithOrderByNameAsc(q)
                .stream()
                .map(s -> s.getName())
                .toList();
    }
}
