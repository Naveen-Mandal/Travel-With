package com.naveenmandal.TravelWith.dto;

public record RegisterRequest(
        String name,
        String phoneNo,
        String password,
        String confirmPassword
) {
}
