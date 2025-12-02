package com.stackdarker.currency_global_time_hub.currency.api;

import com.stackdarker.currency_global_time_hub.currency.model.ConversionResult;
import com.stackdarker.currency_global_time_hub.currency.model.CurrencySymbol;
import com.stackdarker.currency_global_time_hub.currency.model.RatesResponse;
import com.stackdarker.currency_global_time_hub.currency.service.CurrencyService;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/currency")
@Validated
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/symbols")
    public Map<String, CurrencySymbol> getSymbols() {
        return currencyService.getSymbols();
    }

    @GetMapping("/convert")
    public ConversionResult convert(
            @RequestParam @NotBlank String from,
            @RequestParam @NotBlank String to,
            @RequestParam @DecimalMin("0.0") BigDecimal amount
    ) {
        return currencyService.convert(from, to, amount);
    }

    @GetMapping("/rates")
    public RatesResponse getLatestRates(
            @RequestParam(defaultValue = "USD") String base,
            @RequestParam(required = false) List<String> symbols
    ) {
        return currencyService.getLatestRates(base, symbols);
    }
}
