package com.stackdarker.currency_global_time_hub.country.service;

import com.stackdarker.currency_global_time_hub.country.client.CountryClient;
import com.stackdarker.currency_global_time_hub.country.model.CountryIndicators;
import com.stackdarker.currency_global_time_hub.country.model.CountryProfile;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryClient countryClient;

    public CountryServiceImpl(CountryClient countryClient) {
        this.countryClient = countryClient;
    }

    @Override
    @Cacheable("countrySearch")
    public List<CountryProfile> search(String query) {
        return countryClient.searchCountries(query);
    }

    @Override
    @Cacheable("countryProfile")
    public CountryProfile getProfile(String countryCode) {
        return countryClient.getCountryProfile(countryCode);
    }

    @Override
    @Cacheable("countryIndicators")
    public CountryIndicators getIndicators(String countryCode) {
        return countryClient.getCountryIndicators(countryCode);
    }
}
