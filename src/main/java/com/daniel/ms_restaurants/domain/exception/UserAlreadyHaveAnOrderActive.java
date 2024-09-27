package com.daniel.ms_restaurants.domain.exception;

public class UserAlreadyHaveAnOrderActive extends RuntimeException{
    public UserAlreadyHaveAnOrderActive(String message){
        super(message);
    }
}
