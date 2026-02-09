package com.aspora.weather.exception;

import org.springframework.http.HttpStatus;

public class InvalidDateException extends RuntimeException{

    private HttpStatus status;

    public InvalidDateException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
