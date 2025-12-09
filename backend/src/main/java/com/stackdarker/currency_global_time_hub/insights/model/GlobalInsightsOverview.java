package com.stackdarker.currency_global_time_hub.insights.model;

import com.stackdarker.currency_global_time_hub.currency.model.RatesResponse;

import java.time.Instant;
import java.util.List;

public class GlobalInsightsOverview {

    private String baseCurrency;
    private RatesResponse fxMajors;
    private List<PopulationInsight> topPopulation;
    private Instant generatedAt;

    public GlobalInsightsOverview() {
    }

    public GlobalInsightsOverview(String baseCurrency,
                                  RatesResponse fxMajors,
                                  List<PopulationInsight> topPopulation,
                                  Instant generatedAt) {
        this.baseCurrency = baseCurrency;
        this.fxMajors = fxMajors;
        this.topPopulation = topPopulation;
        this.generatedAt = generatedAt;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public RatesResponse getFxMajors() {
        return fxMajors;
    }

    public void setFxMajors(RatesResponse fxMajors) {
        this.fxMajors = fxMajors;
    }

    public List<PopulationInsight> getTopPopulation() {
        return topPopulation;
    }

    public void setTopPopulation(List<PopulationInsight> topPopulation) {
        this.topPopulation = topPopulation;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }
}
