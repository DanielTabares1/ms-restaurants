package com.daniel.ms_restaurants.domain.exception;

public class OrderAndDishNotBelongToTheSameRestaurant extends RuntimeException{
    public OrderAndDishNotBelongToTheSameRestaurant(String message){
        super(message);
    }
}
