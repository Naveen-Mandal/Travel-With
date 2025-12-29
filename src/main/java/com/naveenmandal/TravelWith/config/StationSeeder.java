package com.naveenmandal.TravelWith.config;

import com.naveenmandal.TravelWith.entity.Station;
import com.naveenmandal.TravelWith.repository.StationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "seed.stations.enabled", havingValue = "true")
public class StationSeeder implements CommandLineRunner {

    private final StationRepo stationRepo;

    @Override
    public void run(String... args) throws Exception {
        // Put station names (one per line) in: src/main/resources/stations.txt
        var resource = new ClassPathResource("stations.txt");
        if (!resource.exists()) return;

        Set<String> existing = stationRepo.findAll().stream()
                .map(s -> s.getName().trim())
                .collect(Collectors.toSet());

        List<String> lines = Arrays.asList(new String(resource.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8).split("\\R"));

        List<Station> toInsert = lines.stream()
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .filter(s -> !existing.contains(s))
                .distinct()
                .map(name -> new Station(null, name))
                .toList();

        if (!toInsert.isEmpty()) {
            stationRepo.saveAll(toInsert);
        }
    }
}
