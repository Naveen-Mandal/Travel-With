package com.naveenmandal.TravelWith.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record MatchResponse(
        String phoneNo,
        String name,
        String sourceStation,
        String destinationStation,
        LocalTime trainDepartureTime,
        LocalTime destArrivalTime,
        String trainNo,
        LocalDate journeyDate,
        LocalDate journeyEndDate
) {
}
