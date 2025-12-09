package com.stackdarker.currency_global_time_hub.weather.api;

import com.stackdarker.currency_global_time_hub.weather.model.WeatherForecastResponse;
import com.stackdarker.currency_global_time_hub.weather.model.WeatherSummary;
import com.stackdarker.currency_global_time_hub.weather.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current")
    public ResponseEntity<WeatherSummary> getCurrent(
            @RequestParam("city") String city,
            @RequestParam(name = "countryCode", required = false) String countryCode
    ) {
        WeatherSummary summary;

        if (countryCode == null || countryCode.isBlank()) {
            summary = weatherService.getCurrentWeatherByCity(city);
        } else {
            summary = weatherService.getCurrentWeatherByCity(city, countryCode);
        }

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/forecast")
    public ResponseEntity<WeatherForecastResponse> getForecast(
            @RequestParam("city") String city,
            @RequestParam(name = "hours", required = false, defaultValue = "24") int hours,
            @RequestParam(name = "countryCode", required = false) String countryCode
    ) {
        WeatherForecastResponse forecast;

        if (countryCode == null || countryCode.isBlank()) {
            forecast = weatherService.getForecastByCity(city, hours);
        } else {
            forecast = weatherService.getForecastByCity(city, countryCode, hours);
        }

        return ResponseEntity.ok(forecast);
    }
}
