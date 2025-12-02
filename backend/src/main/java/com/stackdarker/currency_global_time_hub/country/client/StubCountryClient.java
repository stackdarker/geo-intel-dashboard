package com.stackdarker.currency_global_time_hub.country.client;

import com.stackdarker.currency_global_time_hub.country.model.CountryIndicators;
import com.stackdarker.currency_global_time_hub.country.model.CountryProfile;
import com.stackdarker.currency_global_time_hub.country.model.IndicatorValue;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

// temporary stub implementation for development and default profiles
@Component
@Profile({"default", "dev"})
public class StubCountryClient implements CountryClient {

    @Override
    public List<CountryProfile> searchCountries(String query) {
        // always return a small static list that "matches" everything
        return List.of(
                new CountryProfile("US", "United States", "North America",
                        "Washington, D.C.", 331_000_000L,
                        List.of("USD"), List.of("English"),
                        38.8951, -77.0364),
                new CountryProfile("JP", "Japan", "Asia",
                        "Tokyo", 125_800_000L,
                        List.of("JPY"), List.of("Japanese"),
                        35.6895, 139.6917)
        );
    }

    @Override
    public CountryProfile getCountryProfile(String countryCode) {
        // Simple switch until we hook real data
        String code = countryCode.toUpperCase();

        return switch (code) {
            case "US" -> new CountryProfile("US", "United States", "North America",
                    "Washington, D.C.", 331_000_000L,
                    List.of("USD"), List.of("English"),
                    38.8951, -77.0364);
            case "JP" -> new CountryProfile("JP", "Japan", "Asia",
                    "Tokyo", 125_800_000L,
                    List.of("JPY"), List.of("Japanese"),
                    35.6895, 139.6917);
            default -> new CountryProfile(code, "Unknown Country", "Unknown",
                    "Unknown", 0L,
                    List.of(), List.of(),
                    0.0, 0.0);
        };
    }

    @Override
    public CountryIndicators getCountryIndicators(String countryCode) {
        String code = countryCode.toUpperCase();

        // fake numbers for demo purposes
        IndicatorValue gdp = new IndicatorValue(
                "NY.GDP.MKTP.CD",
                "GDP (current US$)",
                code.equals("US") ? 23_000_000_000_000.0 :
                        code.equals("JP") ? 5_000_000_000_000.0 : 0.0,
                2023
        );

        IndicatorValue population = new IndicatorValue(
                "SP.POP.TOTL",
                "Population, total",
                code.equals("US") ? 331_000_000.0 :
                        code.equals("JP") ? 125_800_000.0 : 0.0,
                2023
        );

        IndicatorValue lifeExpectancy = new IndicatorValue(
                "SP.DYN.LE00.IN",
                "Life expectancy at birth, total (years)",
                code.equals("US") ? 78.8 :
                        code.equals("JP") ? 84.5 : 0.0,
                2021
        );

        return new CountryIndicators(code, gdp, population, lifeExpectancy);
    }
}
