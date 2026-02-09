package com.aspora.weather.util;

import com.aspora.weather.dto.HourlyForecast;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WeatherRangeAggregator {

    private static final int RAIN_DIFF_THRESHOLD = 10;
    private static final double WIND_DIFF_THRESHOLD = 5;

    public static List<String> buildReasons(List<HourlyForecast> forecasts) {

        List<String> reasons = new ArrayList<>();

        reasons.addAll(buildThunderstormRanges(forecasts));
        reasons.addAll(buildRainRanges(forecasts));
        reasons.addAll(buildWindRanges(forecasts));

        return reasons;
    }

    private static List<String> buildThunderstormRanges(List<HourlyForecast> forecasts) {
        List<String> result = new ArrayList<>();

        int start = -1;

        for(int i = 0;i<forecasts.size();i++) {
            boolean isStorm = forecasts.get(i).getWeatherCode() >= 95;
            if(isStorm && start == -1) {
                start = i;
            }

            if((!isStorm || i == forecasts.size() - 1) && start != -1) {
                int end = isStorm ? i : i - 1;
                result.add(formatRange(
                        "Thunderstorm expected",
                        forecasts.get(start).getTime(),
                        forecasts.get(end).getTime()
                ));
                start = -1;
            }
        }

        return result;
    }

    private static List<String> buildRainRanges(List<HourlyForecast> forecasts) {
        List<String> result = new ArrayList<>();

        int start = -1;
        int baseValue = 0;

        for(int i = 0;i<forecasts.size();i++) {
            int rain = forecasts.get(i).getRainProbability();

            if(rain >= 60) {
                if(start == -1) {
                    start = i;
                    baseValue = rain;
                }
                else if(Math.abs(rain - baseValue) > RAIN_DIFF_THRESHOLD) {
                    result.add(formatRange(
                            "High rain probability (" + baseValue + "%)",
                            forecasts.get(start).getTime(),
                            forecasts.get(i - 1).getTime()
                    ));
                    start = i;
                    baseValue = rain;
                }
            }

            if((rain < 60 || i == forecasts.size() - 1) && start != -1) {
                int end = rain >= 60 ? i : i - 1;
                result.add(formatRange(
                        "High rain probability (" + baseValue + "%)",
                        forecasts.get(start).getTime(),
                        forecasts.get(end).getTime()
                ));
                start = -1;
            }
        }

        return result;
    }

    private static List<String> buildWindRanges(List<HourlyForecast> forecasts) {
        List<String> result = new ArrayList<>();

        int start = -1;
        double baseValue = 0;

        for(int i = 0;i<forecasts.size();i++) {
            double wind = forecasts.get(i).getWindKmh();

            if(wind >= 25) {
                if(start == -1) {
                    start = i;
                    baseValue = wind;
                }
                else if(Math.abs(wind - baseValue) > WIND_DIFF_THRESHOLD) {
                    result.add(formatRange(
                            "Strong wind (~" + Math.round(baseValue) + " km/h)",
                            forecasts.get(start).getTime(),
                            forecasts.get(i - 1).getTime()
                    ));
                    start = i;
                    baseValue = wind;
                }
            }

            if((wind < 25 || i == forecasts.size() - 1) && start != -1) {
                int end = wind >= 25 ? i : i - 1;
                result.add(formatRange(
                        "Strong wind (~" + Math.round(baseValue) + " km/h)",
                        forecasts.get(start).getTime(),
                        forecasts.get(end).getTime()
                ));
                start = -1;
            }
        }

        return result;
    }

    private static String formatRange(String label,
                                      LocalDateTime start,
                                      LocalDateTime end) {

        String from = start.toLocalTime().toString();
        String to = end.toLocalTime().toString();

        if(from.equals(to)) {
            return label + " at " + from;
        }

        return label + " from " + from + " to " + to;
    }
}
