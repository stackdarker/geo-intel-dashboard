package com.stackdarker.currency_global_time_hub.insights.service;

import com.stackdarker.currency_global_time_hub.insights.model.CountryInsights;
import com.stackdarker.currency_global_time_hub.insights.model.GlobalInsightsOverview;

public interface InsightsService {

    /**
     * @param countryCode  ISO 3166 alpha-2 or alpha-3 
     * @param baseCurrency base FX currency 
     * @param timeZone     IANA zone ID 
     */
    CountryInsights getCountryInsights(String countryCode,
                                       String baseCurrency,
                                       String timeZone);

    GlobalInsightsOverview getGlobalOverview(String baseCurrency);
}
