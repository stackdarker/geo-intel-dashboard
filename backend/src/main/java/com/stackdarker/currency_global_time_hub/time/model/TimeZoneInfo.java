package com.stackdarker.currency_global_time_hub.time.model;

public class TimeZoneInfo {

    private String zoneId; 
    private String region; 
    private String city;   

    public TimeZoneInfo() {
    }

    public TimeZoneInfo(String zoneId, String region, String city) {
        this.zoneId = zoneId;
        this.region = region;
        this.city = city;
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
}
