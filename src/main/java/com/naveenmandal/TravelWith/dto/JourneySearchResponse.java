package com.naveenmandal.TravelWith.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record JourneySearchResponse(
        String trainNo,
        LocalDate journeyDate,
        LocalDate journeyEndDate,
        String sourceStation,
        String destinationStation,
        LocalTime stationTime,
        LocalTime destArrivalTime,
        List<MatchResponse> matchesAtSource,
        List<MatchResponse> matchesAtDestination
) {
}
