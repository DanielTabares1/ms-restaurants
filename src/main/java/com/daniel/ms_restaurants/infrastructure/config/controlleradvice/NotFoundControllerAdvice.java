package com.daniel.ms_restaurants.infrastructure.config.controlleradvice;

import com.daniel.ms_restaurants.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NotFoundControllerAdvice {

    @ExceptionHandler({
            CategoryNotFoundException.class,
            DishNotFoundException.class,
            ResourceNotFoundException.class,
            RestaurantNotFoundException.class,
            OrderDishNotFoundException.class,
            OrderNotFoundException.class,
            OwnerNotFoundException.class,
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFoundExceptions(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
