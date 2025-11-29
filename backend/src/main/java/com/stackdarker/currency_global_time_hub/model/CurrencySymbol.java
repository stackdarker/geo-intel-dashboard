package com.stackdarker.currency_global_time_hub.model;

// model package for Symbols

public class CurrencySymbol {
    private String code;
    private String description;
    
    
    public CurrencySymbol() {

    }

    public CurrencySymbol(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
