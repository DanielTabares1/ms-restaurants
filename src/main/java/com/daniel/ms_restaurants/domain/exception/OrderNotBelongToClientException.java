package com.daniel.ms_restaurants.domain.exception;

public class OrderNotBelongToClientException extends RuntimeException{
    public OrderNotBelongToClientException(String message){
        super(message);
    }
}
