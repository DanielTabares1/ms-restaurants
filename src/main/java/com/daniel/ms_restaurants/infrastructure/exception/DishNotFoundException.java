package com.daniel.ms_restaurants.infrastructure.exception;

public class DishNotFoundException extends RuntimeException{
    public DishNotFoundException(String message){
        super(message);
    }
}
