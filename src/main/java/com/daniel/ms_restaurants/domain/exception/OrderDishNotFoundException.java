package com.daniel.ms_restaurants.domain.exception;

public class OrderDishNotFoundException extends RuntimeException{
    public OrderDishNotFoundException(String message){
        super(message);
    }
}
