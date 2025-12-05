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
    private Double area;
    private String incomeLevel;
    private Double latitude;
    private Double longitude;

    // legacy constructor without area and incomeLevel, in case if used
    public CountryProfile(
            String code,
            String name,
            String region,
            String capital,
            long population,
            List<String> currencies,
            List<String> languages,
            double latitude,
            double longitude) {

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

    // full constructor
    public CountryProfile(
            String code,
            String name,
            String region,
            String capital,
            long population,
            List<String> currencies,
            List<String> languages,
            Double area,
            String incomeLevel,
            Double latitude,
            Double longitude) {

        this.code = code;
        this.name = name;
        this.region = region;
        this.capital = capital;
        this.population = population;
        this.currencies = currencies;
        this.languages = languages;
        this.area = area;
        this.incomeLevel = incomeLevel;
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

    public Double getArea() {
        return area;
    }
    public void setArea(Double area) {
        this.area = area;
    }

    public String getIncomeLevel() {
        return incomeLevel;
    }
    public void setIncomeLevel(String incomeLevel) {
        this.incomeLevel = incomeLevel;
    }

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
