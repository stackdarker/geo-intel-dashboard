package com.stackdarker.currency_global_time_hub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "worldbank.api")
public class WorldBankApiProperties {

    private String baseUrl;
    private String subscriptionKey;
    private String subscriptionKeyHeaderName = "Ocp-Apim-Subscription-Key"; // Azure default

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getSubscriptionKey() {
        return subscriptionKey;
    }

    public void setSubscriptionKey(String subscriptionKey) {
        this.subscriptionKey = subscriptionKey;
    }

    public String getSubscriptionKeyHeaderName() {
        return subscriptionKeyHeaderName;
    }

    public void setSubscriptionKeyHeaderName(String subscriptionKeyHeaderName) {
        this.subscriptionKeyHeaderName = subscriptionKeyHeaderName;
    }
}
