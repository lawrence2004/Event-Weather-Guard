package com.aspora.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class HourlyForecast {
    private LocalDateTime time;
    private int rainProbability;
    private double windKmh;
    private int weatherCode;
}
