package com.stackdarker.currency_global_time_hub.insights.service;

import com.stackdarker.currency_global_time_hub.country.model.CountryIndicators;
import com.stackdarker.currency_global_time_hub.country.model.CountryProfile;
import com.stackdarker.currency_global_time_hub.country.service.CountryService;
import com.stackdarker.currency_global_time_hub.currency.model.RatesResponse;
import com.stackdarker.currency_global_time_hub.currency.service.CurrencyService;
import com.stackdarker.currency_global_time_hub.insights.model.CountryInsights;
import com.stackdarker.currency_global_time_hub.time.model.TimeNowResponse;
import com.stackdarker.currency_global_time_hub.time.service.TimeService;
import com.stackdarker.currency_global_time_hub.weather.model.CurrentWeather;
import com.stackdarker.currency_global_time_hub.weather.service.WeatherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

// implementation of InsightsService that combines data from various services
@Service
public class InsightsServiceImpl implements InsightsService {

    private final CountryService countryService;
    private final CurrencyService currencyService;
    private final WeatherService weatherService;
    private final TimeService timeService;

    public InsightsServiceImpl(CountryService countryService,
                               CurrencyService currencyService,
                               WeatherService weatherService,
                               TimeService timeService) {
        this.countryService = countryService;
        this.currencyService = currencyService;
        this.weatherService = weatherService;
        this.timeService = timeService;
    }

@Override
public CountryInsights getCountryInsights(String countryCode,
                                          String baseCurrency,
                                          String timeZone) {

    String code = countryCode.toUpperCase(Locale.ROOT);
    String base = baseCurrency.toUpperCase(Locale.ROOT);

    CountryProfile country = countryService.getProfile(code);
    CountryIndicators indicators = countryService.getIndicators(code);

    // normalize and filter out base currency from target list. frankfurter API does not allow base to be in targets
    List<String> targetCurrencies = country.getCurrencies().stream()
            .map(c -> c.toUpperCase(Locale.ROOT))
            .filter(c -> !c.equals(base))
            .toList();

    // fallback if there are no other currencies (ex: country only uses base)
    if (targetCurrencies.isEmpty()) {
        targetCurrencies = List.of("EUR", "GBP", "JPY");
    }

    RatesResponse rates = currencyService.getLatestRates(base, targetCurrencies);

    CurrentWeather weather = weatherService.getCurrentWeather(
            country.getLatitude(),
            country.getLongitude()
    );

    TimeNowResponse localTime = timeService.getNow(timeZone);

    return new CountryInsights(country, indicators, rates, weather, localTime);
}

}
