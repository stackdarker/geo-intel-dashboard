package com.stackdarker.currency_global_time_hub.insights.model;

public class PopulationInsight {

    private String code;
    private String name;
    private String region;
    private Long population;

    public PopulationInsight() {
    }

    public PopulationInsight(String code, String name, String region, Long population) {
        this.code = code;
        this.name = name;
        this.region = region;
        this.population = population;
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

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }
}
