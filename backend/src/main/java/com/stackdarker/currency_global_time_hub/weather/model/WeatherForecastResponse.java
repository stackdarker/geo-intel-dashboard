package com.stackdarker.currency_global_time_hub.weather.model;

import java.util.List;

public class WeatherForecastResponse {

    private String city;
    private String country;
    private List<ForecastPoint> points;

    public WeatherForecastResponse() {
    }

    public WeatherForecastResponse(String city, String country, List<ForecastPoint> points) {
        this.city = city;
        this.country = country;
        this.points = points;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public List<ForecastPoint> getPoints() {
        return points;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPoints(List<ForecastPoint> points) {
        this.points = points;
    }
}
