package com.stackdarker.currency_global_time_hub.insights.service;

import com.stackdarker.currency_global_time_hub.insights.model.CountryInsights;

// service interface for generating country insights
public interface InsightsService {

    CountryInsights getCountryInsights(String countryCode,
                                       String baseCurrency,
                                       String timeZone);
}
