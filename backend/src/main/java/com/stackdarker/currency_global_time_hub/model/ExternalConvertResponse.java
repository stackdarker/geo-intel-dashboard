package com.stackdarker.currency_global_time_hub.model;

// external API response model for currency conversion

import java.math.BigDecimal;
import java.util.Map;


public class ExternalConvertResponse {
    private boolean success;
    private Map<String, Object> query; // from ... to .... amount
    private Info info;
    private BigDecimal result;

    public static class Info {
        private long timestamp;
        private BigDecimal rate;

        public long getTimestamp() {
            return timestamp;
        }

        public BigDecimal getRate() {
            return rate;
        }

        public void setRate(BigDecimal rate) {
            this.rate = rate;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public Map<String, Object> getQuery() {
        return query;
    }

    public Info getInfo() {
        return info;
    }


    public BigDecimal getRate() {
        return rate;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setQuery(Map<String, Object> query) {
        this.query = query;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

}
