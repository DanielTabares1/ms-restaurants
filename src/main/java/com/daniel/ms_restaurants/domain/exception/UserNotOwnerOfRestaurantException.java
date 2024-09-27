package com.daniel.ms_restaurants.domain.exception;

public class UserNotOwnerOfRestaurantException extends RuntimeException{
    public UserNotOwnerOfRestaurantException(String message){
        super(message);
    }
}
