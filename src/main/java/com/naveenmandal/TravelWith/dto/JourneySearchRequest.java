package com.naveenmandal.TravelWith.dto;

public record JourneySearchRequest(
        String sourceStation,
        String destinationStation,
        String stationTime,
        String destArrivalTime,
        String trainNo,
        String journeyDate,
        String journeyEndDate
) {
}
