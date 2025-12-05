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
public class RestCountriesClient implements CountryClient {

    private static final String REST_COUNTRIES_URL =
            "https://restcountries.com/v3.1/all?fields=cca2,cca3,name,region,capital,population,currencies,languages,latlng,area";

    private final RestTemplate restTemplate;
    private final WorldBankCountryClient worldBankCountryClient;

    public RestCountriesClient(RestTemplate restTemplate,
                               WorldBankCountryClient worldBankCountryClient) {
        this.restTemplate = restTemplate;
        this.worldBankCountryClient = worldBankCountryClient;
    }

    // get all countries - from Rest Countries API
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
            (rc.getCca2() != null && !rc.getCca2().isBlank()) ? rc.getCca2() : rc.getCca3();

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

        Double lat = null;
        Double lng = null;
        if (rc.getLatlng() != null && rc.getLatlng().size() >= 2) {
            lat = rc.getLatlng().get(0);
            lng = rc.getLatlng().get(1);
        }

        Double area = rc.getArea();
        // set later
        String incomeLevel = null;

        return new CountryProfile(
                code,
                name,
                region,
                capital,
                population,
                currencyCodes,
                languageNames,
                area,
                incomeLevel,
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

    // profile
    @Override
    public CountryProfile getCountryProfile(String countryCode) {
        String codeUpper = countryCode.toUpperCase();
    
        CountryProfile profile = getAllCountries().stream()
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
                        null,  
                        null,     
                        null,     
                        null    
                ));
    
        String incomeLevel = worldBankCountryClient.getIncomeLevel(codeUpper);
        profile.setIncomeLevel(incomeLevel);
    
        return profile;
    }

    @Override
    public CountryIndicators getCountryIndicators(String countryCode) {
        String code = countryCode.toUpperCase();

        IndicatorValue gdp = worldBankCountryClient.getGdpCurrentUsd(code);
        IndicatorValue population = worldBankCountryClient.getPopulationTotal(code);
        IndicatorValue lifeExpectancy = worldBankCountryClient.getLifeExpectancy(code);

        return new CountryIndicators(code, gdp, population, lifeExpectancy);
    }
}
