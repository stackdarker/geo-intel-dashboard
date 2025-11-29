package com.stackdarker.currency_global_time_hub.model;

// model package for response for latest exchange rates

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class RatesResponse {
    private String base;
    private LocalDate date;
    private Map<String, Double> rates;

    public RatesResponse() {
    }

    public RatesResponse(String base, String date, Map<String, BigDecimal> rates) {
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    public java.util.Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRates(java.util.Map<String, Double> rates) {
        this.rates = rates;
    }
}
