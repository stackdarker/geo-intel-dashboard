package com.stackdarker.currency_global_time_hub.currency.api;

import com.stackdarker.currency_global_time_hub.currency.model.ConversionResult;
import com.stackdarker.currency_global_time_hub.currency.model.CurrencySymbol;
import com.stackdarker.currency_global_time_hub.currency.model.RatesResponse;
import com.stackdarker.currency_global_time_hub.currency.service.CurrencyService;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/currency")
@Validated
@CrossOrigin(origins = "http://localhost:4200")
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
        @RequestParam @NotBlank @Pattern(regexp = "^[A-Z]{3}$") String from,
        @RequestParam @NotBlank @Pattern(regexp = "^[A-Z]{3}$") String to,
        @RequestParam @DecimalMin(value = "0.01") BigDecimal amount
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

    @GetMapping("/symbols/list")
    public ResponseEntity<List<CurrencySymbol>> getCurrencySymbols() {
        Map<String, CurrencySymbol> map = currencyService.getSymbols();
        List<CurrencySymbol> list = new ArrayList<>(map.values());
        list.sort(Comparator.comparing(CurrencySymbol::getCode));
        return ResponseEntity.ok(list);
}

}
