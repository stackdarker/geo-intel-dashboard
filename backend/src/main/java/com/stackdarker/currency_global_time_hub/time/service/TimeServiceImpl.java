package com.stackdarker.currency_global_time_hub.time.service;

import com.stackdarker.currency_global_time_hub.common.exception.ExternalApiException;
import com.stackdarker.currency_global_time_hub.time.model.TimeConversionResult;
import com.stackdarker.currency_global_time_hub.time.model.TimeNowResponse;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TimeServiceImpl implements TimeService {

    @Override
    public TimeNowResponse getNow(String zone) {
        try {
            ZoneId zoneId = ZoneId.of(zone);
            ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(), zoneId);
            return new TimeNowResponse(zoneId.getId(), now);
        } catch (DateTimeException ex) {
            throw new ExternalApiException(
                    "JavaTime",
                    "Invalid time zone: " + zone,
                    ex
            );
        }
    }

    @Override
    public TimeConversionResult convert(String fromZone, String toZone, ZonedDateTime dateTime) {
        try {
            ZoneId from = ZoneId.of(fromZone);
            ZoneId to = ZoneId.of(toZone);

            ZonedDateTime original = dateTime.withZoneSameInstant(from);
            ZonedDateTime converted = original.withZoneSameInstant(to);

            return new TimeConversionResult(from.getId(), to.getId(), original, converted);
        } catch (DateTimeException ex) {
            throw new ExternalApiException(
                    "JavaTime",
                    "Invalid time zone(s): " + fromZone + " or " + toZone,
                    ex
            );
        }
    }

    @Override
    public List<String> listZones() {
        Set<String> all = ZoneId.getAvailableZoneIds();
        // sort for stable UI
        return all.stream()
                .sorted()
                .collect(Collectors.toList());
    }
}
