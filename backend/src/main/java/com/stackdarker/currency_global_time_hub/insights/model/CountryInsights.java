package com.stackdarker.currency_global_time_hub.insights.model;

import com.stackdarker.currency_global_time_hub.country.model.CountryIndicators;
import com.stackdarker.currency_global_time_hub.country.model.CountryProfile;
import com.stackdarker.currency_global_time_hub.currency.model.RatesResponse;
import com.stackdarker.currency_global_time_hub.time.model.TimeNowResponse;
import com.stackdarker.currency_global_time_hub.weather.model.WeatherSummary;

public class CountryInsights {

    private CountryProfile country;
    private CountryIndicators indicators;
    private RatesResponse currencyRates;
    private WeatherSummary weather;     
    private TimeNowResponse localTime;

    public CountryInsights() {
    }

    public CountryInsights(CountryProfile country,
                           CountryIndicators indicators,
                           RatesResponse currencyRates,
                           WeatherSummary weather,    
                           TimeNowResponse localTime) {
        this.country = country;
        this.indicators = indicators;
        this.currencyRates = currencyRates;
        this.weather = weather;
        this.localTime = localTime;
    }

    public CountryProfile getCountry() {
        return country;
    }

    public void setCountry(CountryProfile country) {
        this.country = country;
    }

    public CountryIndicators getIndicators() {
        return indicators;
    }

    public void setIndicators(CountryIndicators indicators) {
        this.indicators = indicators;
    }

    public RatesResponse getCurrencyRates() {
        return currencyRates;
    }

    public void setCurrencyRates(RatesResponse currencyRates) {
        this.currencyRates = currencyRates;
    }

    public WeatherSummary getWeather() {         
        return weather;
    }

    public void setWeather(WeatherSummary weather) { 
        this.weather = weather;
    }

    public TimeNowResponse getLocalTime() {
        return localTime;
    }

    public void setLocalTime(TimeNowResponse localTime) {
        this.localTime = localTime;
    }
}
