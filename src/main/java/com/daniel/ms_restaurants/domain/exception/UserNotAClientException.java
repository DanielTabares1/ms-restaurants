package com.daniel.ms_restaurants.domain.exception;

public class UserNotAClientException extends RuntimeException {
    public UserNotAClientException(String message) {
        super(message);
    }
}
