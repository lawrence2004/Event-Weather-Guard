package com.aspora.weather.util;

import com.aspora.weather.dto.HourlyForecast;
import com.aspora.weather.dto.OpenMeteoResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ForecastExtractor {

    public static List<HourlyForecast> extract(
            OpenMeteoResponse response,
            LocalDateTime start,
            LocalDateTime end) {

        List<HourlyForecast> result = new ArrayList<>();

        var hourly = response.getHourly();

        for(int i = 0;i<hourly.getTime().size();i++) {
            LocalDateTime forecastTime =
                    LocalDateTime.parse(hourly.getTime().get(i));
            if(!forecastTime.isBefore(start) &&
                    forecastTime.isBefore(end)) {
                result.add(new HourlyForecast(
                        forecastTime,
                        hourly.getPrecipitation_probability().get(i),
                        hourly.getWind_speed_10m().get(i),
                        hourly.getWeathercode().get(i)
                ));
            }
        }
        return result;
    }
}
