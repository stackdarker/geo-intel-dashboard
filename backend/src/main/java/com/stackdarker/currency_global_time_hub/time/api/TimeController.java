package com.stackdarker.currency_global_time_hub.time.api;

import com.stackdarker.currency_global_time_hub.time.model.TimeNowResponse;
import com.stackdarker.currency_global_time_hub.time.model.TimeZoneInfo;
import com.stackdarker.currency_global_time_hub.time.service.TimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/time")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/now")
    public ResponseEntity<TimeNowResponse> getNow(
            @RequestParam(name = "zone", required = false) String zoneId) {
        TimeNowResponse response = timeService.getNow(zoneId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/zones")
    public ResponseEntity<List<TimeZoneInfo>> getZones() {
        List<TimeZoneInfo> zones = timeService.getAllZones();
        return ResponseEntity.ok(zones);
    }


    @GetMapping("/world-clock")
    public ResponseEntity<List<TimeNowResponse>> getWorldClock(
            @RequestParam(name = "zones") List<String> zones) {
        List<TimeNowResponse> responses = timeService.getWorldClock(zones);
        return ResponseEntity.ok(responses);
    }
}
