package com.stackdarker.currency_global_time_hub.weather.client;

import com.stackdarker.currency_global_time_hub.weather.model.CurrentWeather;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;

// this is a stub implementation of WeatherClient for development and default profiles
@Component
@Profile({"default", "dev"})
public class StubWeatherClient implements WeatherClient {

    @Override
    public CurrentWeather getCurrentWeather(double lat, double lon) {
        return new CurrentWeather(
                lat,
                lon,
                "Clear sky (stub)",
                25.0,
                26.0,
                60,
                3.5,
                Instant.now()
        );
    }
}
