package com.stackdarker.currency_global_time_hub.weather.service;

import com.stackdarker.currency_global_time_hub.weather.model.CurrentWeather;

// service interface for weather-related operations
public interface WeatherService {

    CurrentWeather getCurrentWeather(double lat, double lon);
}
