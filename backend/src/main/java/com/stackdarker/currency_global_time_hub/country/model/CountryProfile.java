package com.stackdarker.currency_global_time_hub.country.model;

import java.util.List;

public class CountryProfile {

    private String code;
    private String name;
    private String region;
    private String capital;
    private long population;
    private List<String> currencies;
    private List<String> languages;
    private double latitude;
    private double longitude;

    
    public CountryProfile(String code, String name, String region, String capital, long population,
            List<String> currencies, List<String> languages, double latitude, double longitude) {
        this.code = code;
        this.name = name;
        this.region = region;
        this.capital = capital;
        this.population = population;
        this.currencies = currencies;
        this.languages = languages;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getCapital() {
        return capital;
    }
    public void setCapital(String capital) {
        this.capital = capital;
    }
    public long getPopulation() {
        return population;
    }
    public void setPopulation(long population) {
        this.population = population;
    }
    public List<String> getCurrencies() {
        return currencies;
    }
    public void setCurrencies(List<String> currencies) {
        this.currencies = currencies;
    }
    public List<String> getLanguages() {
        return languages;
    }
    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    
}
