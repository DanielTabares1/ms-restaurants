package com.daniel.ms_restaurants.infrastructure.exception;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(String message){
        super(message);
    }
}
