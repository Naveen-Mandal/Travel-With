package com.naveenmandal.TravelWith.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ApiInfoController {

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @GetMapping
    public Map<String, String> getApiInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("name", "Travel With API");
        info.put("status", "running");
        info.put("frontend", frontendUrl);
        return info;
    }
}