package com.naveenmandal.TravelWith.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ApiInfoController {

    @GetMapping("/")
    public Map<String, String> apiInfo() {
        return Map.of(
                "name", "Travel With API",
                "status", "running",
                "frontend", "http://localhost:5173"
        );
    }
}
