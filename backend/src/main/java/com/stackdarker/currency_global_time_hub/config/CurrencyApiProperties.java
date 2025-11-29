package com.stackdarker.currency_global_time_hub.config;



@ConfigurationProperties(prefix = "currency-api")
public class CurrencyApiProperties {
    // base URL of the currency API

    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
