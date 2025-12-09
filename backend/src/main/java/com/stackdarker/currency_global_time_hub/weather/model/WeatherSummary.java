package com.stackdarker.currency_global_time_hub.weather.model;

import java.math.BigDecimal;
import java.time.Instant;

public class WeatherSummary {

    private String city;
    private String country;
    private BigDecimal temperature;    
    private BigDecimal feelsLike;     
    private Integer humidity;          
    private BigDecimal windSpeed;      
    private String description;
    private String iconCode;
    private Instant timestamp;

    public WeatherSummary() {
    }

    public WeatherSummary(String city,
                          String country,
                          BigDecimal temperature,
                          BigDecimal feelsLike,
                          Integer humidity,
                          BigDecimal windSpeed,
                          String description,
                          String iconCode,
                          Instant timestamp) {
        this.city = city;
        this.country = country;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.description = description;
        this.iconCode = iconCode;
        this.timestamp = timestamp;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public BigDecimal getFeelsLike() {
        return feelsLike;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public BigDecimal getWindSpeed() {
        return windSpeed;
    }

    public String getDescription() {
        return description;
    }

    public String getIconCode() {
        return iconCode;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public void setFeelsLike(BigDecimal feelsLike) {
        this.feelsLike = feelsLike;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public void setWindSpeed(BigDecimal windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
