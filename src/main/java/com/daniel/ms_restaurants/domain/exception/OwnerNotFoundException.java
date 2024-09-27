package com.daniel.ms_restaurants.domain.exception;

public class OwnerNotFoundException extends RuntimeException{
    public OwnerNotFoundException(String message){
        super(message);
    }
}
