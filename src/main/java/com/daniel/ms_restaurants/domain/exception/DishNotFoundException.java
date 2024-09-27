package com.daniel.ms_restaurants.domain.exception;

public class DishNotFoundException extends RuntimeException{
    public DishNotFoundException(String message){
        super(message);
    }
}
