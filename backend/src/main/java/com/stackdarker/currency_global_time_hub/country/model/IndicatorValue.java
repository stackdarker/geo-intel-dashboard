package com.stackdarker.currency_global_time_hub.country.model;

public class IndicatorValue {

    private String indicatorId;   
    private String indicatorName; 
    private double value;
    private int year;
    
    public IndicatorValue(String indicatorId, String indicatorName, double value, int year) {
        this.indicatorId = indicatorId;
        this.indicatorName = indicatorName;
        this.value = value;
        this.year = year;
    }
    
    public String getIndicatorId() {
        return indicatorId;
    }


    public void setIndicatorId(String indicatorId) {
        this.indicatorId = indicatorId;
    }


    public String getIndicatorName() {
        return indicatorName;
    }


    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }


    public double getValue() {
        return value;
    }


    public void setValue(double value) {
        this.value = value;
    }


    public int getYear() {
        return year;
    }


    public void setYear(int year) {
        this.year = year;
    }



    
}
