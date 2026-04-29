package com.naveenmandal.TravelWith.dto;

public record AuthResponse(
        String token,
        String tokenType,
        String phoneNo,
        String name
) {
}
