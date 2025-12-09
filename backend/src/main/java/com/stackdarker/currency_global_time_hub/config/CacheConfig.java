package com.stackdarker.currency_global_time_hub.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // register every cache used in @Cacheable
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "currencySymbols",
                "latestRates",
                "countrySearch",
                "countryProfile",
                "countryIndicators",
                "currentWeather",
                "weatherForecast",
                "countryAll",
                "historicalRates",
                "timeNow",
                "timeZones",
                "worldClock",
                "exchangeRateSearch",
                "currencyHistoricalRates",
                "currencyExchangeRateSearch",
                "allCurrencies",
                "countryByCurrency",
                "countriesByCurrency",
                "indicatorSearch"
        );

        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(10, TimeUnit.MINUTES)
                        .maximumSize(1000)
        );

        return cacheManager;
    }
}
