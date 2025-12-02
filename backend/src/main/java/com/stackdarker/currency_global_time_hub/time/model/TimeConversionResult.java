package com.stackdarker.currency_global_time_hub.time.model;

import java.time.ZonedDateTime;

// DTO for time conversion result
public class TimeConversionResult {

    private String fromZone;
    private String toZone;
    private ZonedDateTime originalDateTime;
    private ZonedDateTime convertedDateTime;

    public TimeConversionResult() {
    }

    public TimeConversionResult(String fromZone, String toZone,
                                ZonedDateTime originalDateTime,
                                ZonedDateTime convertedDateTime) {
        this.fromZone = fromZone;
        this.toZone = toZone;
        this.originalDateTime = originalDateTime;
        this.convertedDateTime = convertedDateTime;
    }

    public String getFromZone() {
        return fromZone;
    }

    public void setFromZone(String fromZone) {
        this.fromZone = fromZone;
    }

    public String getToZone() {
        return toZone;
    }

    public void setToZone(String toZone) {
        this.toZone = toZone;
    }

    public ZonedDateTime getOriginalDateTime() {
        return originalDateTime;
    }

    public void setOriginalDateTime(ZonedDateTime originalDateTime) {
        this.originalDateTime = originalDateTime;
    }

    public ZonedDateTime getConvertedDateTime() {
        return convertedDateTime;
    }

    public void setConvertedDateTime(ZonedDateTime convertedDateTime) {
        this.convertedDateTime = convertedDateTime;
    }
}
