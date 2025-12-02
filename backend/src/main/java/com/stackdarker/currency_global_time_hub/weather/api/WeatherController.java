package com.stackdarker.currency_global_time_hub.weather.api;

import com.stackdarker.currency_global_time_hub.weather.model.CurrentWeather;
import com.stackdarker.currency_global_time_hub.weather.service.WeatherService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current")
    public CurrentWeather getCurrentWeather(
            @RequestParam double lat,
            @RequestParam double lon
    ) {
        return weatherService.getCurrentWeather(lat, lon);
    }
}
