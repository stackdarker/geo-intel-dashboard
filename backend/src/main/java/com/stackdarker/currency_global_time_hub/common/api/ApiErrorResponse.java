package com.stackdarker.currency_global_time_hub.common.api;

// adjusted API error response to be more detailed and use builder pattern

import java.time.Instant;

public class ApiErrorResponse {

    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private String errorCode;
    private String service;

    public ApiErrorResponse() {
    }

    private ApiErrorResponse(Builder builder) {
        this.timestamp = builder.timestamp;
        this.status = builder.status;
        this.error = builder.error;
        this.message = builder.message;
        this.path = builder.path;
        this.errorCode = builder.errorCode;
        this.service = builder.service;
    }

    public static Builder builder(int status, String error) {
        return new Builder(status, error);
    }

    public static class Builder {
        private final Instant timestamp = Instant.now();
        private final int status;
        private final String error;
        private String message;
        private String path;
        private String errorCode;
        private String service;

        public Builder(int status, String error) {
            this.status = status;
            this.error = error;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder service(String service) {
            this.service = service;
            return this;
        }

        public ApiErrorResponse build() {
            return new ApiErrorResponse(this);
        }
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getService() {
        return service;
    }
}
