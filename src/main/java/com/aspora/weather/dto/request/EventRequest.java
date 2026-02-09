package com.aspora.weather.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRequest {
    private String name;
    private LocationDTO location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

