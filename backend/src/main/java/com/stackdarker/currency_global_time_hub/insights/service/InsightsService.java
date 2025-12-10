package com.stackdarker.currency_global_time_hub.insights.service;

import com.stackdarker.currency_global_time_hub.insights.model.CountryInsights;
import com.stackdarker.currency_global_time_hub.insights.model.GlobalInsightsOverview;
import com.stackdarker.currency_global_time_hub.insights.model.WatchlistInsightsResponse;

import java.util.List;

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

    WatchlistInsightsResponse getWatchlistInsights(List<String> countryCodes,
        String baseCurrency);
}
