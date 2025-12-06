package com.stackdarker.currency_global_time_hub.currency.service;

import com.stackdarker.currency_global_time_hub.common.exception.ExternalApiException;
import com.stackdarker.currency_global_time_hub.config.CurrencyApiProperties;
import com.stackdarker.currency_global_time_hub.currency.model.ConversionResult;
import com.stackdarker.currency_global_time_hub.currency.model.CurrencySymbol;
import com.stackdarker.currency_global_time_hub.currency.model.ExternalLatestRatesResponse;
import com.stackdarker.currency_global_time_hub.currency.model.RatesResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExchangerateHostCurrencyService implements CurrencyService {

    private static final String PROVIDER_NAME = "Frankfurter";

    private final RestTemplate restTemplate;
    private final CurrencyApiProperties properties;

    public ExchangerateHostCurrencyService(RestTemplate restTemplate,
                                           CurrencyApiProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    // resolve and normalize base URL from properties
    private String resolveBaseUrl() {
        String baseUrl = properties.getBaseUrl();

        if (baseUrl == null || baseUrl.isBlank()) {
            throw new ExternalApiException(
                    PROVIDER_NAME,
                    "Base URL is not configured. Please set 'currency.api.base-url' in application.properties"
            );
        }

        if (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
            baseUrl = "https://" + baseUrl;
        }

        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        return baseUrl;
    }

    // fetch currency symbols from Frankfurter (/currencies)
    @Override
    @Cacheable("currencySymbols")
    public Map<String, CurrencySymbol> getSymbols() {
        String baseUrl = resolveBaseUrl();
        String url = baseUrl + "/currencies";

        try {
            ResponseEntity<Map<String, String>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<Map<String, String>>() {}
                    );

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new ExternalApiException(
                        PROVIDER_NAME,
                        "Unexpected response status when fetching currencies: " + response.getStatusCode()
                );
            }

            Map<String, String> raw = response.getBody();

            return raw.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> new CurrencySymbol(e.getKey(), e.getValue())
                    ));

        } catch (RestClientException ex) {
            throw new ExternalApiException(
                    PROVIDER_NAME,
                    "Error calling /currencies",
                    ex
            );
        }
    }

    // convert amount from one currency to another
    @Override
    public ConversionResult convert(String from, String to, BigDecimal amount) {
        String baseUrl = resolveBaseUrl();

        String fromCode = from.toUpperCase(Locale.ROOT);
        String toCode = to.toUpperCase(Locale.ROOT);

        String url = String.format(
                "%s/latest?base=%s&symbols=%s",
                baseUrl,
                fromCode,
                toCode
        );

        try {
            ExternalLatestRatesResponse response =
                    restTemplate.getForObject(url, ExternalLatestRatesResponse.class);

            if (response == null || response.getRates() == null
                    || !response.getRates().containsKey(toCode)) {
                throw new ExternalApiException(
                        PROVIDER_NAME,
                        "Failed to get conversion rate from " + fromCode + " to " + toCode
                );
            }

            BigDecimal rate = response.getRates().get(toCode);
            BigDecimal converted = amount.multiply(rate);

            LocalDate date = LocalDate.parse(response.getDate());
            Instant ts = date.atStartOfDay().toInstant(ZoneOffset.UTC);

            return new ConversionResult(
                    fromCode,
                    toCode,
                    amount,
                    rate,
                    converted,
                    ts
            );

        } catch (RestClientException ex) {
            throw new ExternalApiException(
                    PROVIDER_NAME,
                    "Error calling /latest for conversion",
                    ex
            );
        }
    }

    // fetch latest base currency rates
    @Override
    @Cacheable(
            cacheNames = "latestRates",
            key = "#base.toUpperCase() + '_' + (#symbols == null ? 'ALL' : #symbols.toString())"
    )
    public RatesResponse getLatestRates(String base, List<String> symbols) {
        String baseUrl = resolveBaseUrl();

        StringBuilder url = new StringBuilder(baseUrl).append("/latest");
        boolean hasParam = false;

        if (base != null && !base.isBlank()) {
            url.append(hasParam ? "&" : "?");
            hasParam = true;
            url.append("base=").append(base.toUpperCase(Locale.ROOT));
        }

        if (symbols != null && !symbols.isEmpty()) {
            String joined = symbols.stream()
                    .map(s -> s.toUpperCase(Locale.ROOT))
                    .collect(Collectors.joining(","));
            url.append(hasParam ? "&" : "?");
            url.append("symbols=").append(joined);
        }

        try {
            ExternalLatestRatesResponse response =
                    restTemplate.getForObject(url.toString(), ExternalLatestRatesResponse.class);

            if (response == null || response.getRates() == null) {
                throw new ExternalApiException(
                        PROVIDER_NAME,
                        "Failed to fetch latest rates"
                );
            }

            LocalDate date = LocalDate.parse(response.getDate());
            return new RatesResponse(response.getBase(), date, response.getRates());

        } catch (RestClientException ex) {
            throw new ExternalApiException(
                    PROVIDER_NAME,
                    "Error calling /latest",
                    ex
            );
        }
    }
}
