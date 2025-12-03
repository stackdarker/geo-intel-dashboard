package com.stackdarker.currency_global_time_hub.country.client;

import java.util.List;

import com.stackdarker.currency_global_time_hub.country.model.CountryIndicators;
import com.stackdarker.currency_global_time_hub.country.model.CountryProfile;

public interface CountryClient {

    List<CountryProfile> searchCountries(String query);

    CountryProfile getCountryProfile(String countryCode);

    CountryIndicators getCountryIndicators(String countryCode);

    List<CountryProfile> getAllCountries();
}
