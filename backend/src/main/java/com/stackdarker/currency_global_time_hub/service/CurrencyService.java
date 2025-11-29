package com.stackdarker.currency_global_time_hub.service;

// service to get latest exchange rates

import com.stackdarker.currency_global_time_hub.model.ConversionResult;
import com.stackdarker.currency_global_time_hub.model.CurrencySymbol;
import com.stackdarker.currency_global_time_hub.model.RatesResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CurrencyService {

    Map<String, CurrencySymbol> getSymbols();

    ConversionResult convert(String from, String to, BigDecimal amount);

    RatesResponse getLatestRates(String base, List<String> symbols);
}