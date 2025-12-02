package com.stackdarker.currency_global_time_hub.time.service;

import com.stackdarker.currency_global_time_hub.time.model.TimeConversionResult;
import com.stackdarker.currency_global_time_hub.time.model.TimeNowResponse;

import java.time.ZonedDateTime;
import java.util.List;

// Service interface for time-related stuff
public interface TimeService {

    TimeNowResponse getNow(String zone);

    TimeConversionResult convert(String fromZone, String toZone, ZonedDateTime dateTime);

    List<String> listZones(); // for dropdowns / UI
}
