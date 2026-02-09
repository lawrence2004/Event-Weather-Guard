package com.aspora.weather.controller;

import com.aspora.weather.dto.request.EventRequest;
import com.aspora.weather.dto.response.EventResponse;
import com.aspora.weather.service.EventWeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Event Weather Guard", description = "Weather risk analysis for outdoor events")
@RestController
@RequestMapping("/event-forecast")
public class EventController {

    private final EventWeatherService service;

    public EventController(EventWeatherService service) {
        this.service = service;
    }

    @Operation(
            summary = "Analyze weather risk for an event",
            description = "Returns Safe, Risky, or Unsafe classification based on hourly weather forecast"
    )
    @PostMapping
    public ResponseEntity<EventResponse> getForecast(
            @RequestBody EventRequest request) {

        return ResponseEntity.ok(service.analyzeEvent(request));
    }
}

