package com.daniel.ms_restaurants.infrastructure.exception;

public class UserNotOwnerOfRestaurantException extends RuntimeException{
    public UserNotOwnerOfRestaurantException(String message){
        super(message);
    }
}
