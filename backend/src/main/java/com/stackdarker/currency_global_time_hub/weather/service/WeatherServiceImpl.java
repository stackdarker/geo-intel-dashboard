package com.stackdarker.currency_global_time_hub.weather.service;

import com.stackdarker.currency_global_time_hub.weather.client.WeatherClient;
import com.stackdarker.currency_global_time_hub.weather.model.CurrentWeather;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

// service implementation for weather-related operations
@Service
public class WeatherServiceImpl implements WeatherService {

    private final WeatherClient weatherClient;

    public WeatherServiceImpl(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    @Override
    @Cacheable(
            cacheNames = "currentWeather",
            key = "T(java.lang.String).format('%.4f,%.4f', #lat, #lon)"
    )
    public CurrentWeather getCurrentWeather(double lat, double lon) {
        return weatherClient.getCurrentWeather(lat, lon);
    }
}
