package com.stackdarker.currency_global_time_hub.insights.api;

import com.stackdarker.currency_global_time_hub.insights.model.CountryInsights;
import com.stackdarker.currency_global_time_hub.insights.service.InsightsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/insights")
public class InsightsController {

    private final InsightsService insightsService;

    public InsightsController(InsightsService insightsService) {
        this.insightsService = insightsService;
    }

    @GetMapping("/countries/{code}")
    public ResponseEntity<CountryInsights> getCountryInsights(
            @PathVariable("code") String countryCode,
            @RequestParam(name = "baseCurrency", required = false, defaultValue = "USD") String baseCurrency,
            @RequestParam(name = "timeZone", required = false) String timeZone
    ) {
        CountryInsights insights =
                insightsService.getCountryInsights(countryCode, baseCurrency, timeZone);
        return ResponseEntity.ok(insights);
    }
}
