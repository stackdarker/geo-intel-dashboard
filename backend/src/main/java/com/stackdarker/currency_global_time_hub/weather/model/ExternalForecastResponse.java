package com.stackdarker.currency_global_time_hub.weather.model;

import java.util.List;
import java.util.Map;

public class ExternalForecastResponse {

    private Map<String, Object> city;
    private List<Map<String, Object>> list;

    public Map<String, Object> getCity() {
        return city;
    }

    public void setCity(Map<String, Object> city) {
        this.city = city;
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }
}
