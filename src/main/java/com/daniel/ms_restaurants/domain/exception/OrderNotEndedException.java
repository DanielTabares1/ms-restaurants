package com.daniel.ms_restaurants.domain.exception;

public class OrderNotEndedException extends RuntimeException {
    public OrderNotEndedException(String message) {
        super(message);
    }
}
