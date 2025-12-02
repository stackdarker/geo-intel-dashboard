package com.stackdarker.currency_global_time_hub.country.service;

import com.stackdarker.currency_global_time_hub.country.model.CountryIndicators;
import com.stackdarker.currency_global_time_hub.country.model.CountryProfile;

import java.util.List;

public interface CountryService {

    List<CountryProfile> search(String query);

    CountryProfile getProfile(String countryCode);

    CountryIndicators getIndicators(String countryCode);
}
