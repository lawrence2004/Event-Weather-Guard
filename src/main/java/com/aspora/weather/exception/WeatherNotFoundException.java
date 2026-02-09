package com.aspora.weather.exception;

import org.springframework.http.HttpStatus;

public class WeatherNotFoundException extends RuntimeException{

    private HttpStatus status;

    public WeatherNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}