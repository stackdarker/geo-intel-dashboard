package com.stackdarker.currency_global_time_hub.model;

// model package for latest rates response (external)

import java.math.BigDecimal;
import java.util.Map;

public class ExternalLatestRatesResponse {

    private String base;
    private String date; // e.g. "2025-11-29"
    private Map<String, BigDecimal> rates;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}