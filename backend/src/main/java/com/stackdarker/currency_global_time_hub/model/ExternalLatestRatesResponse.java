package com.stackdarker.currency_global_time_hub.model;

// model package for latest rates response (external)

import java.math.BigDecimal;
import java.util.Map;

public class ExternalLatestRatesResponse {
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;

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

    public void setRates(java.util.Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}
