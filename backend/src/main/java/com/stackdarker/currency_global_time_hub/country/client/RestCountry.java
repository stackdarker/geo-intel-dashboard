package com.stackdarker.currency_global_time_hub.country.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestCountry {

    public static class RestCountryName {
        @JsonProperty("common")
        private String common;

        public String getCommon() { return common; }
        public void setCommon(String common) { this.common = common; }
    }

    public static class RestCurrency {
        private String name;
        private String symbol;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getSymbol() { return symbol; }
        public void setSymbol(String symbol) { this.symbol = symbol; }
    }

    @JsonProperty("cca2")
    private String cca2;

    @JsonProperty("cca3")
    private String cca3;

    @JsonProperty("name")
    private RestCountryName name;

    @JsonProperty("region")
    private String region;

    @JsonProperty("capital")
    private List<String> capital;

    @JsonProperty("population")
    private long population;

    @JsonProperty("currencies")
    private Map<String, RestCurrency> currencies;

    @JsonProperty("languages")
    private Map<String, String> languages;

    @JsonProperty("latlng")
    private List<Double> latlng;

    // getters & setters

    public String getCca2() { return cca2; }
    public void setCca2(String cca2) { this.cca2 = cca2; }

    public String getCca3() { return cca3; }
    public void setCca3(String cca3) { this.cca3 = cca3; }

    public RestCountryName getName() { return name; }
    public void setName(RestCountryName name) { this.name = name; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public List<String> getCapital() { return capital; }
    public void setCapital(List<String> capital) { this.capital = capital; }

    public long getPopulation() { return population; }
    public void setPopulation(long population) { this.population = population; }

    public Map<String, RestCurrency> getCurrencies() { return currencies; }
    public void setCurrencies(Map<String, RestCurrency> currencies) { this.currencies = currencies; }

    public Map<String, String> getLanguages() { return languages; }
    public void setLanguages(Map<String, String> languages) { this.languages = languages; }

    public List<Double> getLatlng() { return latlng; }
    public void setLatlng(List<Double> latlng) { this.latlng = latlng; }
}
