package com.stackdarker.currency_global_time_hub.time.model;

import java.time.Instant;

public class TimeNowResponse {

    private String zoneId;      
    private String region;         
    private String city;           
    private String abbreviation;  
    private String offset;         
    private String localDateTime;  
    private boolean dst;           
    private long epochMillis;      

    public TimeNowResponse() {
    }

    public TimeNowResponse(String zoneId,
                           String region,
                           String city,
                           String abbreviation,
                           String offset,
                           String localDateTime,
                           boolean dst,
                           long epochMillis) {
        this.zoneId = zoneId;
        this.region = region;
        this.city = city;
        this.abbreviation = abbreviation;
        this.offset = offset;
        this.localDateTime = localDateTime;
        this.dst = dst;
        this.epochMillis = epochMillis;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public boolean isDst() {
        return dst;
    }

    public void setDst(boolean dst) {
        this.dst = dst;
    }

    public long getEpochMillis() {
        return epochMillis;
    }

    public void setEpochMillis(long epochMillis) {
        this.epochMillis = epochMillis;
    }
}
