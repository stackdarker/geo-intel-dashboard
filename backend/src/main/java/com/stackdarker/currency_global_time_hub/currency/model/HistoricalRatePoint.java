package com.stackdarker.currency_global_time_hub.currency.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HistoricalRatePoint {

    private LocalDate date;
    private BigDecimal rate;

    public HistoricalRatePoint() {
    }

    public HistoricalRatePoint(LocalDate date, BigDecimal rate) {
        this.date = date;
        this.rate = rate;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
