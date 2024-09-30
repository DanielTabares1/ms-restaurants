package com.daniel.ms_restaurants.domain.exception;

public class UserNotAnEmployeeException extends RuntimeException {
  public UserNotAnEmployeeException(String message) {
    super(message);
  }
}
