package com.daniel.ms_restaurants.domain.exception;

public class InvalidOrderStatusTransitionException extends RuntimeException{
    public InvalidOrderStatusTransitionException(String message){
        super(message);
    }
}
