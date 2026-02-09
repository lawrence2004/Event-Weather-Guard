package com.aspora.weather.service;

import com.aspora.weather.dto.ClassificationResult;
import com.aspora.weather.dto.HourlyForecast;
import com.aspora.weather.dto.OpenMeteoResponse;
import com.aspora.weather.dto.request.EventRequest;
import com.aspora.weather.dto.response.EventResponse;
import com.aspora.weather.dto.response.EventWindowForecast;
import com.aspora.weather.exception.InvalidDateException;
import com.aspora.weather.exception.WeatherNotFoundException;
import com.aspora.weather.util.ForecastExtractor;
import com.aspora.weather.util.OpenMeteoClient;
import com.aspora.weather.util.WeatherClassificationEngine;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventWeatherService {

    private final OpenMeteoClient weatherClient;
    private final WeatherClassificationEngine engine;

    public EventWeatherService(OpenMeteoClient weatherClient,
                               WeatherClassificationEngine engine) {
        this.weatherClient = weatherClient;
        this.engine = engine;
    }

    private static final int MAX_FORECAST_DAYS = 6;

    public EventResponse analyzeEvent(EventRequest request) {

        if(request.getStartTime().isBefore(LocalDateTime.now())) {
            throw new InvalidDateException(
                    "Event start time must be in the future",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new InvalidDateException(
                    "Event start time cannot be greater than end time",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (request.getStartTime().isAfter(LocalDateTime.now().plusDays(MAX_FORECAST_DAYS))) {
            throw new InvalidDateException(
                    "Weather forecast is available only for the next " + MAX_FORECAST_DAYS + " days",
                    HttpStatus.BAD_REQUEST
            );
        }

        OpenMeteoResponse response =
                weatherClient.fetchForecast(
                        request.getLocation().getLatitude(),
                        request.getLocation().getLongitude()
                );

        List<HourlyForecast> forecasts =
                ForecastExtractor.extract(
                        response,
                        request.getStartTime(),
                        request.getEndTime()
                );

        if(forecasts.isEmpty()) {
            throw new WeatherNotFoundException(
                    "Weather forecast not available for the given time window",
                    HttpStatus.NOT_FOUND
            );
        }

        ClassificationResult result = engine.classify(forecasts);

        return EventResponse.builder()
                .classification(result.getClassification())
                .severityScore(result.getSeverityScore())
                .summary(buildSummary(result, forecasts))
                .reason(result.getReasons())
                .eventWindowForecast(mapForecast(forecasts))
                .build();
    }

    private String buildSummary(ClassificationResult result,
                                List<HourlyForecast> forecasts) {

        long highRainHours = forecasts.stream()
                .filter(f -> f.getRainProbability() >= 60)
                .count();

        if(result.getClassification().equals("Unsafe")) {
            return "Severe weather conditions expected during multiple hours of the event";
        }

        if(result.getClassification().equals("Risky")) {
            if(highRainHours == 1) {
                return "High chance of rain during one hour of the event";
            }
            if(highRainHours > 1) {
                return "High chance of rain during multiple hours of the event";
            }
            return "Weather conditions may impact parts of the event";
        }
        return "Weather conditions are suitable throughout the event duration";
    }

    private List<EventWindowForecast> mapForecast(List<HourlyForecast> forecasts) {
        return forecasts.stream()
                .map(f -> new EventWindowForecast(
                        f.getTime().toLocalTime().toString(),
                        f.getRainProbability(),
                        f.getWindKmh()
                ))
                .toList();
    }
}
