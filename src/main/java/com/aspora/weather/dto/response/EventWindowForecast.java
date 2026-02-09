package com.aspora.weather.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventWindowForecast {

    private String time;
    private int rainProb;
    private double windKmh;

}
