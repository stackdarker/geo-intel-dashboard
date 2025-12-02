package com.stackdarker.currency_global_time_hub.weather.client;

import com.stackdarker.currency_global_time_hub.weather.model.CurrentWeather;

// client interface for fetching current weather data
public interface WeatherClient {

    CurrentWeather getCurrentWeather(double lat, double lon);
}
