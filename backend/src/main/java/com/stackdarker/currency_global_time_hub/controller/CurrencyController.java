package com.stackdarker.currency_global_time_hub.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/currency") // maps to /api/currency


public class CurrencyController {

    @GetMapping("/symbols") // maps to /api/currency/symbols
    public Map<String, String> getSymbols() {
        // test, will replace values later
        return Map.of(
            "USD", "United States Dollar",
            "EUR", "Euro",
            "JPY", "Japanese Yen"
        );
    }
    
}
