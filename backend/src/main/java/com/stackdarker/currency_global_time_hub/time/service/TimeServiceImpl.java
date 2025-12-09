package com.stackdarker.currency_global_time_hub.time.service;

import com.stackdarker.currency_global_time_hub.common.exception.ExternalApiException;
import com.stackdarker.currency_global_time_hub.time.model.TimeNowResponse;
import com.stackdarker.currency_global_time_hub.time.model.TimeZoneInfo;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TimeServiceImpl implements TimeService {

    private static final String PROVIDER_NAME = "JavaTime";

    private static final DateTimeFormatter ABBR_FORMATTER =
            DateTimeFormatter.ofPattern("zzz", Locale.ENGLISH);

    private static final DateTimeFormatter LOCAL_DT_FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public TimeNowResponse getNow(String zone) {
        try {
            ZoneId zoneId;
            if (zone == null || zone.isBlank()) {
                zoneId = ZoneId.systemDefault();
            } else {
                zoneId = ZoneId.of(zone);
            }

            ZonedDateTime now = ZonedDateTime.now(zoneId);
            Instant instant = now.toInstant();
            ZoneOffset offset = now.getOffset();

            boolean dst = zoneId.getRules().isDaylightSavings(instant);
            String abbr = now.format(ABBR_FORMATTER);

            String fullZoneId = zoneId.getId();
            String region = fullZoneId;
            String city = "";
            int slashIndex = fullZoneId.indexOf('/');
            if (slashIndex > 0 && slashIndex < fullZoneId.length() - 1) {
                region = fullZoneId.substring(0, slashIndex);
                city = fullZoneId.substring(slashIndex + 1);
            }

            return new TimeNowResponse(
                    fullZoneId,
                    region,
                    city,
                    abbr,
                    offset.toString(),                              
                    now.toLocalDateTime().format(LOCAL_DT_FORMATTER),
                    dst,
                    instant.toEpochMilli()
            );
        } catch (DateTimeException ex) {
            throw new ExternalApiException(
                    PROVIDER_NAME,
                    "Invalid time zone: " + zone,
                    ex
            );
        }
    }

    @Override
    public List<TimeZoneInfo> getAllZones() {
        Set<String> all = ZoneId.getAvailableZoneIds();

        return all.stream()
                .map(id -> {
                    String region = id;
                    String city = "";
                    int slashIndex = id.indexOf('/');
                    if (slashIndex > 0 && slashIndex < id.length() - 1) {
                        region = id.substring(0, slashIndex);
                        city = id.substring(slashIndex + 1);
                    }
                    return new TimeZoneInfo(id, region, city);
                })
                .sorted(Comparator
                        .comparing(TimeZoneInfo::getRegion)
                        .thenComparing(TimeZoneInfo::getCity)
                        .thenComparing(TimeZoneInfo::getZoneId))
                .collect(Collectors.toList());
    }

    @Override
    public List<TimeNowResponse> getWorldClock(List<String> zoneIds) {
        if (zoneIds == null || zoneIds.isEmpty()) {
            return List.of();
        }

        return zoneIds.stream()
                .filter(z -> z != null && !z.isBlank())
                .distinct()
                .sorted()
                .map(this::getNow)   
                .collect(Collectors.toList());
    }
}
