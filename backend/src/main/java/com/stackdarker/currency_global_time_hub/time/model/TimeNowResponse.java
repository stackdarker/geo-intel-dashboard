package com.stackdarker.currency_global_time_hub.time.model;

import java.time.ZonedDateTime;

// Simple DTO for time now response

public class TimeNowResponse {

    private String zone;
    private ZonedDateTime dateTime;

    public TimeNowResponse() {
    }

    public TimeNowResponse(String zone, ZonedDateTime dateTime) {
        this.zone = zone;
        this.dateTime = dateTime;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
