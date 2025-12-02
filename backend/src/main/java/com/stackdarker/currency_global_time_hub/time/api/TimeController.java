package com.stackdarker.currency_global_time_hub.time.api;

import com.stackdarker.currency_global_time_hub.time.model.TimeConversionResult;
import com.stackdarker.currency_global_time_hub.time.model.TimeNowResponse;
import com.stackdarker.currency_global_time_hub.time.service.TimeService;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

// REST controller for time-related endpoints
@Validated
@RestController
@RequestMapping("/api/v1/time")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/now")
    public TimeNowResponse getNow(@RequestParam(defaultValue = "UTC") String zone) {
        return timeService.getNow(zone);
    }

    @GetMapping("/convert")
    public TimeConversionResult convert(
            @RequestParam String fromZone,
            @RequestParam String toZone,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime dateTime
    ) {
        return timeService.convert(fromZone, toZone, dateTime);
    }

    @GetMapping("/zones")
    public List<String> getZones() {
        return timeService.listZones();
    }
}
