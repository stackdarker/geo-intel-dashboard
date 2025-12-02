package com.stackdarker.currency_global_time_hub.weather.api;

import com.stackdarker.currency_global_time_hub.weather.model.CurrentWeather;
import com.stackdarker.currency_global_time_hub.weather.service.WeatherService;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current")
    public CurrentWeather getCurrentWeather(
        @RequestParam @DecimalMin("-90") @DecimalMax("90") double lat,
        @RequestParam @DecimalMin("-180") @DecimalMax("180") double lon
    ) {
        return weatherService.getCurrentWeather(lat, lon);
    }
}
