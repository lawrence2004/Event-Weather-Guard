package com.aspora.weather.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonPropertyOrder({
        "classification",
        "severityScore",
        "summary",
        "reason",
        "eventWindowForecast"
})
public class EventResponse {

    private String classification;
    private int severityScore;
    private String summary;
    private List<String> reason;
    private List<EventWindowForecast> eventWindowForecast;

}
