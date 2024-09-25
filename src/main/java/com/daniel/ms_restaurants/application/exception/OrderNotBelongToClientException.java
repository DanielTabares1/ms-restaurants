package com.daniel.ms_restaurants.application.exception;

public class OrderNotBelongToClientException extends RuntimeException{
    public OrderNotBelongToClientException(String message){
        super(message);
    }
}
