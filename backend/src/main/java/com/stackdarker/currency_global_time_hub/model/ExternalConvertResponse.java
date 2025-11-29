package com.stackdarker.currency_global_time_hub.model;


// external API response model for currency conversion

import java.math.BigDecimal;

public class ExternalConvertResponse {

    private boolean success;
    private Query query;
    private Info info;
    private BigDecimal result;

    public static class Query {
        private String from;
        private String to;
        private BigDecimal amount;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }

    public static class Info {
        private long timestamp;
        private BigDecimal rate;

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public BigDecimal getRate() {
            return rate;
        }

        public void setRate(BigDecimal rate) {
            this.rate = rate;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }
}
