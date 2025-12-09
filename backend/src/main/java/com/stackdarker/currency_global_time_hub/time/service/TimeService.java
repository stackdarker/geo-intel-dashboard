package com.stackdarker.currency_global_time_hub.time.service;

import com.stackdarker.currency_global_time_hub.time.model.TimeNowResponse;
import com.stackdarker.currency_global_time_hub.time.model.TimeZoneInfo;

import java.util.List;

public interface TimeService {

   
    TimeNowResponse getNow(String zoneId);
    
    List<TimeZoneInfo> getAllZones();

    List<TimeNowResponse> getWorldClock(List<String> zoneIds);
}
