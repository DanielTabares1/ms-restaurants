package com.daniel.ms_restaurants.domain.exception;

public enum ErrorMessages {
    // General errors
    RESOURCE_NOT_FOUND("Resource not found for: %s"),

    // User-related errors
    USER_ALREADY_HAS_ACTIVE_ORDER("User already has an active order"),
    USER_NOT_OWNER_OF_RESTAURANT("User is not the owner of the restaurant with ID: %d"),
    USER_NOT_OWNER_OF_RESTAURANT_WITH_DISH("User is not the owner of the restaurant that offers dish with ID: %d"),

    // Restaurant-related errors
    RESTAURANT_NOT_FOUND("Restaurant not found with ID: %d"),
    OWNER_NOT_FOUND("Owner not found or user is not an owner"),

    // Order-related errors
    ORDER_NOT_FOUND("Order not found with ID: %d"),
    ORDER_NOT_BELONG_TO_CLIENT("Order with ID: %d does not belong to client with email: %s"),
    ORDER_AND_DISH_NOT_BELONG_TO_SAME_RESTAURANT("Order and dish do not belong to the same restaurant"),
    ORDER_DISH_NOT_FOUND("Order dish not found with ID: %d"),

    // Category-related errors
    CATEGORY_NOT_FOUND("Category not found with ID: %d"),

    // Dish-related errors
    DISH_NOT_FOUND("Dish not found with ID: %d"),

    // Order status transition related errors
    INVALID_ORDER_STATUS_TRANSITION_EXCEPTION_FROM_TO("Invalid transition from %s to %s"),
    INVALID_ORDER_STATUS_TRANSITION_EXCEPTION_FROM("Invalid transition from %s");


    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
