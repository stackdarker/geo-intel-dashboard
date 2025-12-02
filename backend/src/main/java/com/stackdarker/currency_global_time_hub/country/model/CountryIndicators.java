package com.stackdarker.currency_global_time_hub.country.model;

public class CountryIndicators {

    private String countryCode;
    private IndicatorValue gdp;
    private IndicatorValue population;
    private IndicatorValue lifeExpectancy;


    public CountryIndicators(String countryCode, IndicatorValue gdp, IndicatorValue population,
            IndicatorValue lifeExpectancy) {
        this.countryCode = countryCode;
        this.gdp = gdp;
        this.population = population;
        this.lifeExpectancy = lifeExpectancy;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public IndicatorValue getGdp() {
        return gdp;
    }
    public void setGdp(IndicatorValue gdp) {
        this.gdp = gdp;
    }
    public IndicatorValue getPopulation() {
        return population;
    }
    public void setPopulation(IndicatorValue population) {
        this.population = population;
    }
    public IndicatorValue getLifeExpectancy() {
        return lifeExpectancy;
    }
    public void setLifeExpectancy(IndicatorValue lifeExpectancy) {
        this.lifeExpectancy = lifeExpectancy;
    }
}
