package com.stackdarker.currency_global_time_hub.country.api;

import com.stackdarker.currency_global_time_hub.country.model.CountryIndicators;
import com.stackdarker.currency_global_time_hub.country.model.CountryProfile;
import com.stackdarker.currency_global_time_hub.country.service.CountryService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/countries")
@CrossOrigin(origins = "http://localhost:4200")  // allow Angular dev
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    // get all countries
    @GetMapping("/all")
    public List<CountryProfile> getAllCountries() {
        return countryService.getAllCountries();
    }

    // search countries by name or partial name
    @GetMapping("/search")
    public List<CountryProfile> searchCountries(
            @RequestParam
            @NotBlank(message = "Search query must not be blank")
            @Size(min = 2, max = 50, message = "Search query must be between 2 and 50 characters")
            String q
    ) {
        return countryService.search(q);
    }

    // get country profile by ISO alpha-2/alpha-3 code
    @GetMapping("/{code}")
    public CountryProfile getCountryProfile(
            @PathVariable
            @NotBlank(message = "Country code must not be blank")
            @Pattern(regexp = "^[A-Za-z]{2,3}$",
                     message = "Country code must be 2–3 letters (ISO alpha-2/alpha-3)")
            String code
    ) {
        return countryService.getProfile(code);
    }

    // get country indicators by ISO alpha-2/alpha-3 code
    @GetMapping("/{code}/indicators")
    public CountryIndicators getCountryIndicators(
            @PathVariable
            @NotBlank(message = "Country code must not be blank")
            @Pattern(regexp = "^[A-Za-z]{2,3}$",
                     message = "Country code must be 2–3 letters (ISO alpha-2/alpha-3)")
            String code
    ) {
        return countryService.getIndicators(code);
    }
}
