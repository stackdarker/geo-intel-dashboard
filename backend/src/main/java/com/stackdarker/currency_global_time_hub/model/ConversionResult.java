package com.stackdarker.currency_global_time_hub.model;

import java.math.BigDecimal;
import java.time.Instant;


// model package for Conversion Result

public class ConversionResult {
    private String from;
    private String to;
    private BigDecimal amount;
    private BigDecimal rate;
    private BigDecimal result;
    private Instant timestamp;

    public ConversionResult() {
    }

    public ConversionResult(String from, String to, BigDecimal amount, BigDecimal rate,
        BigDecimal result, Instant timestamp) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.rate = rate;
        this.result = result;
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public BigDecimal getResult() {
        return result;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
