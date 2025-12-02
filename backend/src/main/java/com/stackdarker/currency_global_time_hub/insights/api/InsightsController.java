package com.stackdarker.currency_global_time_hub.insights.api;

import com.stackdarker.currency_global_time_hub.insights.model.CountryInsights;
import com.stackdarker.currency_global_time_hub.insights.service.InsightsService;
import org.springframework.web.bind.annotation.*;

// REST controller for country insights endpoints
@RestController
@RequestMapping("/api/v1/insights")
public class InsightsController {

    private final InsightsService insightsService;

    public InsightsController(InsightsService insightsService) {
        this.insightsService = insightsService;
    }

    @GetMapping("/country/{code}")
    public CountryInsights getCountryInsights(
            @PathVariable String code,
            @RequestParam(defaultValue = "USD") String baseCurrency,
            @RequestParam(defaultValue = "UTC") String timeZone
    ) {
        return insightsService.getCountryInsights(code, baseCurrency, timeZone);
    }
}
