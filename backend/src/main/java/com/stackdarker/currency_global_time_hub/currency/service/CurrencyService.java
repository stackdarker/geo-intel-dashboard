package com.stackdarker.currency_global_time_hub.currency.service;

import com.stackdarker.currency_global_time_hub.currency.model.ConversionResult;
import com.stackdarker.currency_global_time_hub.currency.model.CurrencySymbol;
import com.stackdarker.currency_global_time_hub.currency.model.RatesResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CurrencyService {

    Map<String, CurrencySymbol> getSymbols();

    ConversionResult convert(String from, String to, BigDecimal amount);

    RatesResponse getLatestRates(String base, List<String> symbols);
}