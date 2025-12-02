package com.stackdarker.currency_global_time_hub.weather.model;

import java.time.Instant;

// DTO for current weather data
public class CurrentWeather {

    private double latitude;
    private double longitude;
    private String description;
    private double temperatureCelsius;
    private double feelsLikeCelsius;
    private int humidity;
    private double windSpeed;
    private Instant observedAt;

    public CurrentWeather() {
    }

    public CurrentWeather(double latitude, double longitude, String description,
                          double temperatureCelsius, double feelsLikeCelsius,
                          int humidity, double windSpeed, Instant observedAt) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.temperatureCelsius = temperatureCelsius;
        this.feelsLikeCelsius = feelsLikeCelsius;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.observedAt = observedAt;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public void setTemperatureCelsius(double temperatureCelsius) {
        this.temperatureCelsius = temperatureCelsius;
    }

    public double getFeelsLikeCelsius() {
        return feelsLikeCelsius;
    }

    public void setFeelsLikeCelsius(double feelsLikeCelsius) {
        this.feelsLikeCelsius = feelsLikeCelsius;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Instant getObservedAt() {
        return observedAt;
    }

    public void setObservedAt(Instant observedAt) {
        this.observedAt = observedAt;
    }
}
