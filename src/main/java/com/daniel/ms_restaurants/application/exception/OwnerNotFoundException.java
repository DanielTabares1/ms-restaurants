package com.daniel.ms_restaurants.application.exception;

public class OwnerNotFoundException extends RuntimeException{
    public OwnerNotFoundException(String message){
        super(message);
    }
}
