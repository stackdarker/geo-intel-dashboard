package com.stackdarker.currency_global_time_hub.country.api;

import com.stackdarker.currency_global_time_hub.country.model.CountryIndicators;
import com.stackdarker.currency_global_time_hub.country.model.CountryProfile;
import com.stackdarker.currency_global_time_hub.country.service.CountryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/search")
    public List<CountryProfile> searchCountries(@RequestParam String q) {
        return countryService.search(q);
    }

    @GetMapping("/{code}")
    public CountryProfile getCountryProfile(@PathVariable String code) {
        return countryService.getProfile(code);
    }

    @GetMapping("/{code}/indicators")
    public CountryIndicators getCountryIndicators(@PathVariable String code) {
        return countryService.getIndicators(code);
    }
}
