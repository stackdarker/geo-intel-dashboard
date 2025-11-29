package com.stackdarker.currency_global_time_hub.model;

import java.util.Map;

// api response wrapper

public class ExternalSymbolsResponse {
    private boolean success;
    private Map<String, ExternalSymbol> symbols;
    
    public boolean isSuccess() {
        return success;
    }

    public Map<String, ExternalSymbol> getSymbols() {
        return symbols;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setSymbols(Map<String, ExternalSymbol> symbols) {
        this.symbols = symbols;
    }

    public static class ExternalSymbol {
        private String description;
        private String code;

        public String getDescription() {
            return description;
        }

        public String getCode() {
            return code;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
