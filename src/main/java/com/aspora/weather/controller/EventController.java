package com.aspora.weather.controller;

import com.aspora.weather.dto.request.EventRequest;
import com.aspora.weather.dto.response.EventResponse;
import com.aspora.weather.service.EventWeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event-forecast")
public class EventController {

    private final EventWeatherService service;

    public EventController(EventWeatherService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EventResponse> getForecast(
            @RequestBody EventRequest request) {

        return ResponseEntity.ok(service.analyzeEvent(request));
    }
}

