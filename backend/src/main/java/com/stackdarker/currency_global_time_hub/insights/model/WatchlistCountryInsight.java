package com.stackdarker.currency_global_time_hub.insights.model;

import java.math.BigDecimal;

public class WatchlistCountryInsight {

    private String code;
    private String name;
    private String region;
    private long population;
    private String currency;
    private BigDecimal fxRate;

    public WatchlistCountryInsight() {
    }

    public WatchlistCountryInsight(String code,
                                   String name,
                                   String region,
                                   long population,
                                   String currency,
                                   BigDecimal fxRate) {
        this.code = code;
        this.name = name;
        this.region = region;
        this.population = population;
        this.currency = currency;
        this.fxRate = fxRate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getFxRate() {
        return fxRate;
    }

    public void setFxRate(BigDecimal fxRate) {
        this.fxRate = fxRate;
    }
}
