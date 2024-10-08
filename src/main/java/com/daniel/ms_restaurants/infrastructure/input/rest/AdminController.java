package com.daniel.ms_restaurants.infrastructure.input.rest;

import com.daniel.ms_restaurants.application.dto.CreateRestaurantRequest;
import com.daniel.ms_restaurants.application.handler.IRestaurantHandler;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.infrastructure.input.rest.constants.ApiEndpoints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiEndpoints.ADMIN_API)
public class AdminController {

    private final IRestaurantHandler restaurantHandler;


    @PostMapping("/restaurant")
    @Operation(summary = "Add a new restaurant",
            description = "Creates a new restaurant and returns the created restaurant details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurant created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Restaurant> addNewRestaurant(@RequestBody CreateRestaurantRequest request){
        Restaurant createdRestaurant = restaurantHandler.saveRestaurant(request);
        return new ResponseEntity<>(createdRestaurant, HttpStatus.CREATED);
    }

}
