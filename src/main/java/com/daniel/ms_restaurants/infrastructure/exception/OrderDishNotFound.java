package com.daniel.ms_restaurants.infrastructure.exception;

public class OrderDishNotFound extends RuntimeException{
    public OrderDishNotFound(String message){
        super(message);
    }
}
