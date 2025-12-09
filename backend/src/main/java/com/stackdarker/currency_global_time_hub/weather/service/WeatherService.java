package com.stackdarker.currency_global_time_hub.weather.service;

import com.stackdarker.currency_global_time_hub.weather.model.WeatherForecastResponse;
import com.stackdarker.currency_global_time_hub.weather.model.WeatherSummary;

public interface WeatherService {

    WeatherSummary getCurrentWeatherByCity(String city);
    WeatherSummary getCurrentWeatherByCity(String city, String countryCode);

    WeatherForecastResponse getForecastByCity(String city, int hours);
    WeatherForecastResponse getForecastByCity(String city, String countryCode, int hours);

}
