package com.daniel.ms_restaurants.application.exception;

public class OrderAndDishNotBelongToTheSameRestaurant extends RuntimeException{
    public OrderAndDishNotBelongToTheSameRestaurant(String message){
        super(message);
    }
}
