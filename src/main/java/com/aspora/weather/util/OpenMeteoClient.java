package com.aspora.weather.util;

import com.aspora.weather.dto.OpenMeteoResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenMeteoClient {

    private final RestTemplate restTemplate;

    public OpenMeteoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public OpenMeteoResponse fetchForecast(double lat, double lon) {

        String url = String.format(
                "https://api.open-meteo.com/v1/forecast" +
                        "?latitude=%f&longitude=%f" +
                        "&hourly=precipitation_probability,wind_speed_10m,weathercode" +
                        "&timezone=auto",
                lat, lon
        );

        return restTemplate.getForObject(url, OpenMeteoResponse.class);
    }
}
