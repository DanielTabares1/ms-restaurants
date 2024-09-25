package com.daniel.ms_restaurants.application.exception;

public class UserAlreadyHaveAnOrderActive extends RuntimeException{
    public UserAlreadyHaveAnOrderActive(String message){
        super(message);
    }
}
