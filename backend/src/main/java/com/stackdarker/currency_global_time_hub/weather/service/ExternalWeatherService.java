package com.stackdarker.currency_global_time_hub.weather.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stackdarker.currency_global_time_hub.common.exception.ExternalApiException;
import com.stackdarker.currency_global_time_hub.config.WeatherApiProperties;
import com.stackdarker.currency_global_time_hub.weather.model.ForecastPoint;
import com.stackdarker.currency_global_time_hub.weather.model.WeatherForecastResponse;
import com.stackdarker.currency_global_time_hub.weather.model.WeatherSummary;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ExternalWeatherService implements WeatherService {

    private static final String PROVIDER_NAME = "open-meteo";

    private final RestTemplate restTemplate;
    private final WeatherApiProperties properties;

    public ExternalWeatherService(RestTemplate restTemplate,
                                  WeatherApiProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    private String resolveBaseUrl() {
        String baseUrl = properties.getBaseUrl();

        if (baseUrl == null || baseUrl.isBlank()) {
            throw new ExternalApiException(
                    PROVIDER_NAME,
                    "Weather API base URL is not configured. Please set 'weather.api.base-url' in application.properties"
            );
        }

        if (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
            baseUrl = "https://" + baseUrl;
        }

        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        return baseUrl;
    }

    private String resolveGeocodingUrl() {
        String geoUrl = properties.getGeocodingUrl();
        if (geoUrl == null || geoUrl.isBlank()) {
            throw new ExternalApiException(
                    PROVIDER_NAME,
                    "Geocoding API base URL is not configured. Please set 'weather.api.geocoding-url' in application.properties"
            );
        }

        if (!geoUrl.startsWith("http://") && !geoUrl.startsWith("https://")) {
            geoUrl = "https://" + geoUrl;
        }

        if (geoUrl.endsWith("/")) {
            geoUrl = geoUrl.substring(0, geoUrl.length() - 1);
        }

        return geoUrl;
    }


@Override
@Cacheable(
        cacheNames = "currentWeather",
        key = "#countryCode != null && !#countryCode.isBlank() ? "
            + "#city.toLowerCase() + '_' + #countryCode.toUpperCase() "
            + ": #city.toLowerCase()"
)
public WeatherSummary getCurrentWeatherByCity(String city, String countryCode) {
    GeoLocation location = geocodeCity(city, countryCode);
    String baseUrl = resolveBaseUrl();

    String url = String.format(
            "%s?latitude=%f&longitude=%f&current_weather=true"
                    + "&hourly=temperature_2m,apparent_temperature,relative_humidity_2m,weather_code"
                    + "&forecast_hours=1&timezone=auto",
            baseUrl,
            location.latitude,
            location.longitude
    );

    try {
        OpenMeteoForecastResponse external =
                restTemplate.getForObject(url, OpenMeteoForecastResponse.class);

        if (external == null || external.getCurrentWeather() == null) {
            throw new ExternalApiException(
                    PROVIDER_NAME,
                    "Unexpected response from current weather endpoint"
            );
        }

        OpenMeteoForecastResponse.CurrentWeather current = external.getCurrentWeather();
        OpenMeteoForecastResponse.Hourly hourly = external.getHourly();

        BigDecimal temp = toBigDecimal(current.getTemperature());
        BigDecimal feelsLike = temp;
        Integer humidity = null;

        if (hourly != null) {
            if (hourly.getApparentTemperature() != null && !hourly.getApparentTemperature().isEmpty()) {
                feelsLike = toBigDecimal(hourly.getApparentTemperature().get(0));
            }
            if (hourly.getRelativeHumidity2m() != null && !hourly.getRelativeHumidity2m().isEmpty()) {
                humidity = hourly.getRelativeHumidity2m().get(0);
            }
        }

        BigDecimal windSpeed = toBigDecimal(current.getWindSpeed());
        int code = current.getWeatherCode();
        String description = mapWeatherCodeToDescription(code);
        String icon = mapWeatherCodeToIcon(code);

        Instant ts = parseInstantOrNow(current.getTime());

        return new WeatherSummary(
                location.name,
                location.countryCode,
                temp,
                feelsLike,
                humidity,
                windSpeed,
                description,
                icon,
                ts
        );

    } catch (RestClientException ex) {
        throw new ExternalApiException(
                PROVIDER_NAME,
                "Error calling current weather endpoint",
                ex
        );
    }
}

@Override
@Cacheable(
        cacheNames = "weatherForecast",
        key = "#countryCode != null && !#countryCode.isBlank() ? "
            + "#city.toLowerCase() + '_' + #countryCode.toUpperCase() + '_' + #hours "
            + ": #city.toLowerCase() + '_' + #hours"
)
public WeatherForecastResponse getForecastByCity(String city, String countryCode, int hours) {
    if (hours <= 0) {
        hours = 24;
    } else if (hours > 168) { 
        hours = 168;
    }

    GeoLocation location = geocodeCity(city, countryCode);
    String baseUrl = resolveBaseUrl();

    String url = String.format(
            "%s?latitude=%f&longitude=%f&hourly=temperature_2m,apparent_temperature,"
                    + "relative_humidity_2m,weather_code"
                    + "&forecast_hours=%d&timezone=auto",
            baseUrl,
            location.latitude,
            location.longitude,
            hours
    );

    try {
        OpenMeteoForecastResponse external =
                restTemplate.getForObject(url, OpenMeteoForecastResponse.class);

        if (external == null || external.getHourly() == null || external.getHourly().getTime() == null) {
            throw new ExternalApiException(
                    PROVIDER_NAME,
                    "Unexpected response from forecast endpoint"
            );
        }

        OpenMeteoForecastResponse.Hourly hourly = external.getHourly();
        List<String> times = hourly.getTime();

        List<Double> temps = safeList(hourly.getTemperature2m(), times.size());
        List<Double> feels = safeList(hourly.getApparentTemperature(), times.size());
        List<Integer> hums = safeList(hourly.getRelativeHumidity2m(), times.size());
        List<Integer> codes = safeList(hourly.getWeatherCode(), times.size());

        List<ForecastPoint> points = new ArrayList<>(times.size());

        for (int i = 0; i < times.size(); i++) {
            String timeStr = times.get(i);
            Instant ts = parseInstantOrNow(timeStr);

            BigDecimal temp = (i < temps.size() && temps.get(i) != null)
                    ? toBigDecimal(temps.get(i))
                    : null;

            BigDecimal feel = (i < feels.size() && feels.get(i) != null)
                    ? toBigDecimal(feels.get(i))
                    : temp;

            Integer hum = (i < hums.size()) ? hums.get(i) : null;
            int code = (i < codes.size() && codes.get(i) != null) ? codes.get(i) : 0;

            String description = mapWeatherCodeToDescription(code);
            String icon = mapWeatherCodeToIcon(code);

            points.add(new ForecastPoint(ts, temp, description, icon));
        }

        return new WeatherForecastResponse(location.name, location.countryCode, points);

    } catch (RestClientException ex) {
        throw new ExternalApiException(
                PROVIDER_NAME,
                "Error calling forecast endpoint",
                ex
        );
    }
}


private GeoLocation geocodeCity(String city, String countryCode) {
    GeoLocation loc = geocodeCityOnce(city, countryCode);

    if (loc == null && countryCode != null && !countryCode.isBlank()) {
        loc = geocodeCityOnce(city, null);
    }

    if (loc == null) {
        throw new ExternalApiException(
                PROVIDER_NAME,
                "City '" + city + "' not found in geocoding API"
                        + (countryCode != null ? " for country '" + countryCode + "'" : "")
        );
    }

    return loc;
}

private GeoLocation geocodeCityOnce(String city, String countryCode) {
    String geoBase = resolveGeocodingUrl();

    StringBuilder sb = new StringBuilder()
            .append(geoBase)
            .append("?name=")
            .append(URLEncoder.encode(city, StandardCharsets.UTF_8))
            .append("&count=1&language=en&format=json");

    if (countryCode != null && !countryCode.isBlank()) {
        sb.append("&countryCode=")
          .append(URLEncoder.encode(countryCode.toUpperCase(Locale.ROOT), StandardCharsets.UTF_8));
    }

    String url = sb.toString();

    try {
        OpenMeteoGeocodingResponse response =
                restTemplate.getForObject(url, OpenMeteoGeocodingResponse.class);

        if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
            return null; 
        }

        OpenMeteoGeocodingResponse.Location r = response.getResults().get(0);

        return new GeoLocation(
                r.getName(),
                r.getCountryCode(),
                r.getLatitude(),
                r.getLongitude(),
                r.getTimezone()
        );

    } catch (RestClientException ex) {
        throw new ExternalApiException(
                PROVIDER_NAME,
                "Error calling geocoding endpoint",
                ex
        );
    }
}




    private static class GeoLocation {
        final String name;
        final String countryCode;
        final double latitude;
        final double longitude;
        final String timezone;

        GeoLocation(String name, String countryCode, double latitude, double longitude, String timezone) {
            this.name = name;
            this.countryCode = countryCode;
            this.latitude = latitude;
            this.longitude = longitude;
            this.timezone = timezone;
        }
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof Number) {
            return new BigDecimal(((Number) value).toString());
        }
        if (value instanceof String) {
            try {
                return new BigDecimal((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    private Instant parseInstantOrNow(String time) {
    if (time == null || time.isBlank()) {
        return Instant.now();
    }

    try {
        return Instant.parse(time);
    } catch (DateTimeParseException ignored) {
        try {
            LocalDateTime ldt = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return ldt.toInstant(ZoneOffset.UTC);
        } catch (DateTimeParseException ignored2) {
            return Instant.now();
        }
    }
    }

    private static <T> List<T> safeList(List<T> list, int maxSize) {
        if (list == null) {
            return List.of();
        }
        if (list.size() > maxSize) {
            return list.subList(0, maxSize);
        }
        return list;
    }

    private String mapWeatherCodeToDescription(int code) {
        return switch (code) {
            case 0 -> "Clear";
            case 1 -> "Mostly clear";
            case 2 -> "Partly cloudy";
            case 3 -> "Cloudy";
            case 45 -> "Fog";
            case 48 -> "Freezing fog";
            case 51 -> "Light drizzle";
            case 53 -> "Drizzle";
            case 55 -> "Heavy drizzle";
            case 56 -> "Light freezing drizzle";
            case 57 -> "Freezing drizzle";
            case 61 -> "Light rain";
            case 63 -> "Rain";
            case 65 -> "Heavy rain";
            case 66 -> "Light freezing rain";
            case 67 -> "Freezing rain";
            case 71 -> "Light snow";
            case 73 -> "Snow";
            case 75 -> "Heavy snow";
            case 77 -> "Snow grains";
            case 80 -> "Light rain shower";
            case 81 -> "Rain shower";
            case 82 -> "Heavy rain shower";
            case 85 -> "Snow shower";
            case 86 -> "Heavy snow shower";
            case 95 -> "Thunderstorm";
            case 96 -> "Thunderstorm with hail";
            case 99 -> "Severe thunderstorm with hail";
            default -> "Unknown";
        };
    }

    private String mapWeatherCodeToIcon(int code) {
        if (code == 0) return "clear";
        if (code == 1 || code == 2) return "partly-cloudy";
        if (code == 3) return "cloudy";
        if (code == 45 || code == 48) return "fog";
        if (code >= 51 && code <= 57) return "drizzle";
        if ((code >= 61 && code <= 67) || (code >= 80 && code <= 82)) return "rain";
        if ((code >= 71 && code <= 77) || code == 85 || code == 86) return "snow";
        if (code >= 95) return "thunderstorm";
        return "unknown";
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OpenMeteoGeocodingResponse {
        private List<Location> results;

        public List<Location> getResults() {
            return results;
        }

        public void setResults(List<Location> results) {
            this.results = results;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class Location {
            private String name;

            @JsonProperty("country_code")
            private String countryCode;

            private double latitude;
            private double longitude;
            private String timezone;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCountryCode() {
                return countryCode;
            }

            public void setCountryCode(String countryCode) {
                this.countryCode = countryCode;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public String getTimezone() {
                return timezone;
            }

            public void setTimezone(String timezone) {
                this.timezone = timezone;
            }
        }
    }
    

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OpenMeteoForecastResponse {

        @JsonProperty("current_weather")
        private CurrentWeather currentWeather;

        private Hourly hourly;

        public CurrentWeather getCurrentWeather() {
            return currentWeather;
        }

        public void setCurrentWeather(CurrentWeather currentWeather) {
            this.currentWeather = currentWeather;
        }

        public Hourly getHourly() {
            return hourly;
        }

        public void setHourly(Hourly hourly) {
            this.hourly = hourly;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class CurrentWeather {
            private String time;
            private double temperature;

            @JsonProperty("windspeed")
            private double windSpeed;

            @JsonProperty("weathercode")
            private int weatherCode;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public double getTemperature() {
                return temperature;
            }

            public void setTemperature(double temperature) {
                this.temperature = temperature;
            }

            public double getWindSpeed() {
                return windSpeed;
            }

            public void setWindSpeed(double windSpeed) {
                this.windSpeed = windSpeed;
            }

            public int getWeatherCode() {
                return weatherCode;
            }

            public void setWeatherCode(int weatherCode) {
                this.weatherCode = weatherCode;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class Hourly {
            private List<String> time;

            @JsonProperty("temperature_2m")
            private List<Double> temperature2m;

            @JsonProperty("apparent_temperature")
            private List<Double> apparentTemperature;

            @JsonProperty("relative_humidity_2m")
            private List<Integer> relativeHumidity2m;

            @JsonProperty("weather_code")
            private List<Integer> weatherCode;

            public List<String> getTime() {
                return time;
            }

            public void setTime(List<String> time) {
                this.time = time;
            }

            public List<Double> getTemperature2m() {
                return temperature2m;
            }

            public void setTemperature2m(List<Double> temperature2m) {
                this.temperature2m = temperature2m;
            }

            public List<Double> getApparentTemperature() {
                return apparentTemperature;
            }

            public void setApparentTemperature(List<Double> apparentTemperature) {
                this.apparentTemperature = apparentTemperature;
            }

            public List<Integer> getRelativeHumidity2m() {
                return relativeHumidity2m;
            }

            public void setRelativeHumidity2m(List<Integer> relativeHumidity2m) {
                this.relativeHumidity2m = relativeHumidity2m;
            }

            public List<Integer> getWeatherCode() {
                return weatherCode;
            }

            public void setWeatherCode(List<Integer> weatherCode) {
                this.weatherCode = weatherCode;
            }
        }
    }

    @Override
    public WeatherSummary getCurrentWeatherByCity(String city) {
    // Delegate to the new overload 
    return getCurrentWeatherByCity(city, null);
}

    @Override
    public WeatherForecastResponse getForecastByCity(String city, int hours) {
    // Delegate to the new overload 
    return getForecastByCity(city, null, hours);
}

}
