package com.stackdarker.currency_global_time_hub.country.client;

import com.stackdarker.currency_global_time_hub.country.model.IndicatorValue;

public interface WorldBankCountryClient {

    IndicatorValue getGdpCurrentUsd(String countryCode);

    IndicatorValue getPopulationTotal(String countryCode);

    IndicatorValue getLifeExpectancy(String countryCode);

    String getIncomeLevel(String countryCode);
}
