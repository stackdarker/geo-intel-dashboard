package com.stackdarker.currency_global_time_hub.currency.model;

import java.util.Map;

public class ExternalSymbolsResponse {

    private boolean success;
    private Map<String, CurrencySymbol> symbols;

    public ExternalSymbolsResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, CurrencySymbol> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<String, CurrencySymbol> symbols) {
        this.symbols = symbols;
    }
}
