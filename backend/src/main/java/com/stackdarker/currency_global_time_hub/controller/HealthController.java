package com.stackdarker.currency_global_time_hub.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController // handles http and returns JSON
@RequestMapping("/api") // base path for endpoints

public class HealthController {
    
    @GetMapping("/health") // handles GET/api/health
    public Map<String, Object> health() {
        return Map.of(  // ..... builds JSON object
                "status", "UP",
                "timestamp", OffsetDateTime.now().toString(),
                "service", "currency_global_time_hub_backend"
        );
    }
}
