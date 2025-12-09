package com.stackdarker.currency_global_time_hub.weather.model;

import java.math.BigDecimal;
import java.time.Instant;

public class ForecastPoint {

    private Instant timestamp;
    private BigDecimal temperature;
    private String description;
    private String iconCode;

    public ForecastPoint() {
    }

    public ForecastPoint(Instant timestamp,
                         BigDecimal temperature,
                         String description,
                         String iconCode) {
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.description = description;
        this.iconCode = iconCode;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public String getIconCode() {
        return iconCode;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }
}
