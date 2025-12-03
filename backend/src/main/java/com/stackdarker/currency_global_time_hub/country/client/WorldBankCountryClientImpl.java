package com.stackdarker.currency_global_time_hub.country.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.stackdarker.currency_global_time_hub.config.WorldBankApiProperties;
import com.stackdarker.currency_global_time_hub.country.model.IndicatorValue;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class WorldBankCountryClientImpl implements WorldBankCountryClient {

    private final RestTemplate restTemplate;
    private final WorldBankApiProperties worldBankApiProperties;

    public WorldBankCountryClientImpl(RestTemplate restTemplate,
                                      WorldBankApiProperties worldBankApiProperties) {
        this.restTemplate = restTemplate;
        this.worldBankApiProperties = worldBankApiProperties;
    }

    @Override
    public IndicatorValue getGdpCurrentUsd(String countryCode) {
        return fetchLatestIndicator(
                countryCode,
                "NY.GDP.MKTP.CD",
                "GDP (current US$)"
        );
    }

    @Override
    public IndicatorValue getPopulationTotal(String countryCode) {
        return fetchLatestIndicator(
                countryCode,
                "SP.POP.TOTL",
                "Population, total"
        );
    }

    @Override
    public IndicatorValue getLifeExpectancy(String countryCode) {
        return fetchLatestIndicator(
                countryCode,
                "SP.DYN.LE00.IN",
                "Life expectancy at birth, total (years)"
        );
    }

    private IndicatorValue fetchLatestIndicator(String countryCode,
                                                String indicatorCode,
                                                String indicatorName) {

        String code = countryCode.toLowerCase();
        String baseUrl = worldBankApiProperties.getBaseUrl(); // ex: https://api.worldbank.org/v2

        String url = baseUrl +
                "/country/" + code +
                "/indicator/" + indicatorCode +
                "?format=json&per_page=10";

        try {
            JsonNode root = restTemplate.getForObject(url, JsonNode.class);
            if (root == null || !root.isArray() || root.size() < 2) {
                return new IndicatorValue(indicatorCode, indicatorName, 0.0, 0);
            }

            JsonNode dataArray = root.get(1);
            if (dataArray == null || !dataArray.isArray() || dataArray.size() == 0) {
                return new IndicatorValue(indicatorCode, indicatorName, 0.0, 0);
            }

            // find the most recent non-null value
            for (JsonNode entry : dataArray) {
                JsonNode valueNode = entry.get("value");
                if (valueNode != null && !valueNode.isNull()) {
                    double value = valueNode.asDouble();
                    int year = entry.hasNonNull("date")
                            ? Integer.parseInt(entry.get("date").asText())
                            : 0;

                    return new IndicatorValue(
                            indicatorCode,
                            indicatorName,
                            value,
                            year
                    );
                }
            }

            return new IndicatorValue(indicatorCode, indicatorName, 0.0, 0);
        } catch (RestClientException ex) {
            // network / timeout / I/O errors, just "no data"
            return new IndicatorValue(indicatorCode, indicatorName, 0.0, 0);
        } catch (Exception ex) {
            // any unexpected parsing errors
            return new IndicatorValue(indicatorCode, indicatorName, 0.0, 0);
        }
    }
}
