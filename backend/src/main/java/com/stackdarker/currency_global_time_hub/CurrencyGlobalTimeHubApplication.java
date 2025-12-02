package com.stackdarker.currency_global_time_hub;

import com.stackdarker.currency_global_time_hub.config.CurrencyApiProperties;
import com.stackdarker.currency_global_time_hub.config.WeatherApiProperties;
import com.stackdarker.currency_global_time_hub.config.WorldBankApiProperties;
import com.stackdarker.currency_global_time_hub.config.TimezoneApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableConfigurationProperties({
    CurrencyApiProperties.class,
    WeatherApiProperties.class,
    WorldBankApiProperties.class,
    TimezoneApiProperties.class
})
@EnableCaching
public class CurrencyGlobalTimeHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyGlobalTimeHubApplication.class, args);
    }
}
