package com.stackdarker.currency_global_time_hub.country.client;

import com.stackdarker.currency_global_time_hub.country.model.CountryIndicators;
import com.stackdarker.currency_global_time_hub.country.model.CountryProfile;
import com.stackdarker.currency_global_time_hub.country.model.IndicatorValue;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Profile({"default", "dev"})
public class StubCountryClient implements CountryClient {

    private static final String REST_COUNTRIES_URL =
            "https://restcountries.com/v3.1/all?fields=cca2,cca3,name,region,capital,population,currencies,languages,latlng";

    private final RestTemplate restTemplate;

    public StubCountryClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // get all countries
    @Override
    public List<CountryProfile> getAllCountries() {
        RestCountry[] response = restTemplate.getForObject(REST_COUNTRIES_URL, RestCountry[].class);

        if (response == null || response.length == 0) {
            return List.of();
        }

        List<CountryProfile> result = new ArrayList<>(response.length);

        for (RestCountry rc : response) {
            result.add(toCountryProfile(rc));
        }

        return result;
    }

    // mapping
    private CountryProfile toCountryProfile(RestCountry rc) {
        String code =
                (rc.getCca3() != null && !rc.getCca3().isBlank()) ? rc.getCca3() : rc.getCca2();

        String name =
                (rc.getName() != null && rc.getName().getCommon() != null)
                        ? rc.getName().getCommon()
                        : code;

        String region =
                (rc.getRegion() != null) ? rc.getRegion() : "Unknown";

        String capital =
                (rc.getCapital() != null && !rc.getCapital().isEmpty())
                        ? rc.getCapital().get(0)
                        : "Unknown";

        long population = rc.getPopulation();

        List<String> currencyCodes = List.of();
        Map<String, RestCountry.RestCurrency> currencies = rc.getCurrencies();
        if (currencies != null && !currencies.isEmpty()) {
            currencyCodes = new ArrayList<>(currencies.keySet());
        }

        List<String> languageNames = List.of();
        Map<String, String> languages = rc.getLanguages();
        if (languages != null && !languages.isEmpty()) {
            languageNames = new ArrayList<>(languages.values());
        }

        double lat = 0.0;
        double lng = 0.0;
        if (rc.getLatlng() != null && rc.getLatlng().size() >= 2) {
            lat = rc.getLatlng().get(0);
            lng = rc.getLatlng().get(1);
        }

        return new CountryProfile(
                code,
                name,
                region,
                capital,
                population,
                currencyCodes,
                languageNames,
                lat,
                lng
        );
    }

    // search
    @Override
    public List<CountryProfile> searchCountries(String query) {
        String q = query.toLowerCase();
        return getAllCountries().stream()
                .filter(c ->
                        c.getName().toLowerCase().contains(q) ||
                        c.getCode().toLowerCase().contains(q)
                )
                .toList();
    }

    // get profile
    @Override
    public CountryProfile getCountryProfile(String countryCode) {
        String codeUpper = countryCode.toUpperCase();
        return getAllCountries().stream()
                .filter(c -> c.getCode().equalsIgnoreCase(codeUpper))
                .findFirst()
                .orElseGet(() -> new CountryProfile(
                        codeUpper,
                        "Unknown Country",
                        "Unknown",
                        "Unknown",
                        0L,
                        List.of(),
                        List.of(),
                        0.0,
                        0.0
                ));
    }

    // hook to world bank
    @Override
    public CountryIndicators getCountryIndicators(String countryCode) {
        // wire this to real World Bank client next
        return new CountryIndicators(
                countryCode.toUpperCase(),
                new IndicatorValue("NY.GDP.MKTP.CD", "GDP", 0.0, 2023),
                new IndicatorValue("SP.POP.TOTL", "Population", 0.0, 2023),
                new IndicatorValue("SP.DYN.LE00.IN", "Life Expectancy", 0.0, 2021)
        );
    }
}
