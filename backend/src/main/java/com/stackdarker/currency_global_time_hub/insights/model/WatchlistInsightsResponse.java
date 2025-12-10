package com.stackdarker.currency_global_time_hub.insights.model;

import java.time.Instant;
import java.util.List;

public class WatchlistInsightsResponse {

    private String baseCurrency;
    private List<WatchlistCountryInsight> items;
    private Instant generatedAt;

    public WatchlistInsightsResponse() {
    }

    public WatchlistInsightsResponse(String baseCurrency,
                                     List<WatchlistCountryInsight> items,
                                     Instant generatedAt) {
        this.baseCurrency = baseCurrency;
        this.items = items;
        this.generatedAt = generatedAt;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public List<WatchlistCountryInsight> getItems() {
        return items;
    }

    public void setItems(List<WatchlistCountryInsight> items) {
        this.items = items;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }
}
