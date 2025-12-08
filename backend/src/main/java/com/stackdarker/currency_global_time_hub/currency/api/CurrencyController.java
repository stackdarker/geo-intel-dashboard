package com.stackdarker.currency_global_time_hub.currency.api;

import com.stackdarker.currency_global_time_hub.currency.model.ConversionResult;
import com.stackdarker.currency_global_time_hub.currency.model.CurrencySymbol;
import com.stackdarker.currency_global_time_hub.currency.model.RatesResponse;
import com.stackdarker.currency_global_time_hub.currency.service.CurrencyService;
import com.stackdarker.currency_global_time_hub.currency.model.HistoricalRatePoint;


import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;


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

    @GetMapping("/rates")
    public RatesResponse getLatestRates(
            @RequestParam(defaultValue = "USD") String base,
            @RequestParam(required = false) List<String> symbols
    ) {
        return currencyService.getLatestRates(base, symbols);
    }

    @GetMapping("/history")
    public ResponseEntity<List<HistoricalRatePoint>> getHistoricalRates(
        @RequestParam("from") String from,
        @RequestParam("to") String to,
        @RequestParam(name = "days", defaultValue = "30") int days
    ) {
    if (days <= 0) {
        return ResponseEntity.badRequest().build();
    }

    List<HistoricalRatePoint> points = currencyService.getHistoricalRates(from, to, days);
    return ResponseEntity.ok(points);
    }


    @GetMapping("/symbols/list")
    public ResponseEntity<List<CurrencySymbol>> getCurrencySymbols() {
        Map<String, CurrencySymbol> map = currencyService.getSymbols();
        List<CurrencySymbol> list = new ArrayList<>(map.values());
        list.sort(Comparator.comparing(CurrencySymbol::getCode));
        return ResponseEntity.ok(list);
}
    @GetMapping("/convert")
    public ResponseEntity<ConversionResult> convert(
        @RequestParam("from") String from,
        @RequestParam("to") String to,
        @RequestParam("amount") BigDecimal amount
        ) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Amount must be greater than zero"
        );
    }

    ConversionResult result = currencyService.convert(from, to, amount);
    return ResponseEntity.ok(result);

    
}


}
