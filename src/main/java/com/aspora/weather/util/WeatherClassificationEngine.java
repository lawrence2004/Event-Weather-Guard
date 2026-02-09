package com.aspora.weather.util;

import com.aspora.weather.dto.ClassificationResult;
import com.aspora.weather.dto.HourlyForecast;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeatherClassificationEngine {

    public ClassificationResult classify(List<HourlyForecast> forecasts) {

        boolean unsafe = false;
        boolean risky = false;
        for(HourlyForecast f : forecasts) {
            if(f.getWeatherCode() >= 95 || f.getRainProbability() >= 80 || f.getWindKmh() >= 40) {
                unsafe = true;
            }
            else if(f.getRainProbability() >= 60 || f.getWindKmh() >= 25) {
                risky = true;
            }
        }
        String classification = unsafe ? "Unsafe" : risky ? "Risky" : "Safe";

        List<String> reasons =
                classification.equals("Safe")
                        ? List.of("No significant weather risks detected")
                        : WeatherRangeAggregator.buildReasons(forecasts);

        int severity = calculate(forecasts);

        return new ClassificationResult(classification, reasons, severity);
    }


    public static int calculate(List<HourlyForecast> forecasts) {

        double maxRainScore = 0;
        double maxWindScore = 0;
        boolean thunderstorm = false;

        for(HourlyForecast f : forecasts) {
            double rainScore = (f.getRainProbability() / 100.0) * 40;
            maxRainScore = Math.max(maxRainScore, rainScore);
            double windScore = Math.min(f.getWindKmh() / 50.0, 1.0) * 30;
            maxWindScore = Math.max(maxWindScore, windScore);
            if (f.getWeatherCode() >= 95) {
                thunderstorm = true;
            }
        }

        double stormScore = thunderstorm ? 30 : 0;

        int total = (int)Math.round(maxRainScore + maxWindScore + stormScore);

        return Math.min(total, 100);
    }
}
