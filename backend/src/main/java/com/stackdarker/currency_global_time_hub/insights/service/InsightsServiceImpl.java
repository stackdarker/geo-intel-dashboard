package com.stackdarker.currency_global_time_hub.insights.service;

import com.stackdarker.currency_global_time_hub.country.model.CountryIndicators;
import com.stackdarker.currency_global_time_hub.country.model.CountryProfile;
import com.stackdarker.currency_global_time_hub.country.service.CountryService;
import com.stackdarker.currency_global_time_hub.currency.model.RatesResponse;
import com.stackdarker.currency_global_time_hub.currency.service.CurrencyService;
import com.stackdarker.currency_global_time_hub.insights.model.CountryInsights;
import com.stackdarker.currency_global_time_hub.time.model.TimeNowResponse;
import com.stackdarker.currency_global_time_hub.time.service.TimeService;
import com.stackdarker.currency_global_time_hub.weather.model.WeatherSummary;
import com.stackdarker.currency_global_time_hub.weather.service.WeatherService;
import org.springframework.stereotype.Service;
import com.stackdarker.currency_global_time_hub.insights.model.GlobalInsightsOverview;
import com.stackdarker.currency_global_time_hub.insights.model.PopulationInsight;
import com.stackdarker.currency_global_time_hub.insights.model.WatchlistCountryInsight;
import com.stackdarker.currency_global_time_hub.insights.model.WatchlistInsightsResponse;

import java.time.Instant;
import java.util.Comparator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        String base = (baseCurrency == null || baseCurrency.isBlank())
                ? "USD"
                : baseCurrency.toUpperCase(Locale.ROOT);

        CountryProfile country = countryService.getProfile(code);
        CountryIndicators indicators = countryService.getIndicators(code);

        List<String> targetCurrencies = country.getCurrencies().stream()
                .map(c -> c.toUpperCase(Locale.ROOT))
                .filter(c -> !c.equals(base))
                .toList();

        if (targetCurrencies.isEmpty()) {
            targetCurrencies = List.of("EUR", "GBP", "JPY");
        }

        RatesResponse rates = currencyService.getLatestRates(base, targetCurrencies);

        WeatherSummary weather = null;
        if (country.getLatitude() != null && country.getLongitude() != null) {
            weather = weatherService.getCurrentWeatherByCity(country.getCapital());
        }

        String tz = (timeZone == null || timeZone.isBlank())
                ? "UTC"
                : timeZone;

        TimeNowResponse localTime = timeService.getNow(tz);

        return new CountryInsights(country, indicators, rates, weather, localTime);
    }

    @Override
public GlobalInsightsOverview getGlobalOverview(String baseCurrency) {
    String base = (baseCurrency == null || baseCurrency.isBlank())
            ? "USD"
            : baseCurrency.toUpperCase(Locale.ROOT);

    var allCountries = countryService.getAllCountries(); 

    var topPopulation = allCountries.stream()
            .sorted(Comparator.comparingLong(
                    c -> -c.getPopulation()  
            ))
            .limit(5)
            .map(c -> new PopulationInsight(
                    c.getCode(),
                    c.getName(),
                    c.getRegion(),
                    c.getPopulation()
            ))
            .toList();

    var majors = List.of("EUR", "GBP", "JPY", "CHF", "AUD", "CAD");
    var targets = majors.stream()
            .map(m -> m.toUpperCase(Locale.ROOT))
            .filter(m -> !m.equals(base))
            .toList();

    var fxMajors = currencyService.getLatestRates(base, targets);

    return new GlobalInsightsOverview(
            base,
            fxMajors,
            topPopulation,
            Instant.now()
    );
}

@Override
public WatchlistInsightsResponse getWatchlistInsights(List<String> countryCodes,
                                                      String baseCurrency) {
    if (countryCodes == null || countryCodes.isEmpty()) {
        return new WatchlistInsightsResponse(
                baseCurrency != null ? baseCurrency.toUpperCase(Locale.ROOT) : "USD",
                List.of(),
                Instant.now()
        );
    }

    String base = (baseCurrency == null || baseCurrency.isBlank())
            ? "USD"
            : baseCurrency.toUpperCase(Locale.ROOT);

    Set<String> uniqueCodes = new HashSet<>();
    for (String code : countryCodes) {
        if (code != null && !code.isBlank()) {
            uniqueCodes.add(code.toUpperCase(Locale.ROOT));
        }
    }

    if (uniqueCodes.isEmpty()) {
        return new WatchlistInsightsResponse(base, List.of(), Instant.now());
    }

    var profiles = new ArrayList<CountryProfile>();
    for (String code : uniqueCodes) {
        profiles.add(countryService.getProfile(code));
    }

    Set<String> targetCurrencies = new HashSet<>();
    var countryCurrencyMap = new ArrayList<String>(); 

    for (CountryProfile profile : profiles) {
        String mainCurrency = null;
        if (profile.getCurrencies() != null && !profile.getCurrencies().isEmpty()) {
            mainCurrency = profile.getCurrencies().get(0).toUpperCase(Locale.ROOT);
        }

        countryCurrencyMap.add(mainCurrency);

        if (mainCurrency != null && !mainCurrency.equals(base)) {
            targetCurrencies.add(mainCurrency);
        }
    }

    RatesResponse fx = targetCurrencies.isEmpty()
            ? new RatesResponse(base, null, Map.of()) 
            : currencyService.getLatestRates(base, new ArrayList<>(targetCurrencies));

    var resultItems = new ArrayList<WatchlistCountryInsight>();

    for (int i = 0; i < profiles.size(); i++) {
        CountryProfile profile = profiles.get(i);
        String mainCurrency = countryCurrencyMap.get(i);

        BigDecimal rate = null;
        if (mainCurrency != null) {
            if (mainCurrency.equals(base)) {
                rate = BigDecimal.ONE;
            } else if (fx.getRates() != null) {
                rate = fx.getRates().get(mainCurrency);
            }
        }

        long population = profile.getPopulation(); 

        resultItems.add(new WatchlistCountryInsight(
                profile.getCode(),
                profile.getName(),
                profile.getRegion(),
                population,
                mainCurrency,
                rate
        ));
    }

    return new WatchlistInsightsResponse(base, resultItems, Instant.now());
}


}
