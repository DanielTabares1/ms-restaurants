package com.daniel.ms_restaurants.infrastructure.input.rest;

import com.daniel.ms_restaurants.application.dto.CreateDishRequest;
import com.daniel.ms_restaurants.application.dto.EditDishRequest;
import com.daniel.ms_restaurants.application.dto.ToggleActivationToDishRequest;
import com.daniel.ms_restaurants.application.handler.IDishHandler;
import com.daniel.ms_restaurants.domain.model.Dish;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class OwnerController {

    private final IDishHandler dishHandler;


    @PostMapping("/dish")
    @Operation(summary = "Add a new dish",
            description = "Creates a new dish and returns the created dish details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dish created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Dish> createDish(
            @Valid @RequestBody CreateDishRequest dishRequest
    ){
        Dish newDish = dishHandler.saveDish(dishRequest);
        return new ResponseEntity<>(newDish, HttpStatus.CREATED);
    }

    @PutMapping("/dish/{id}")
    @Operation(summary = "Edit an existing dish",
            description = "Edit an existing dish and returns the edited dish details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish edited successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Dish not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Dish> editDish(
            @Valid @PathVariable Long id,
            @Valid @RequestBody EditDishRequest dishRequest
    ){
        Dish editedDish = dishHandler.editDish(id, dishRequest);
        return new ResponseEntity<>(editedDish, HttpStatus.OK);
    }

    @PutMapping("/dish/toggleActivated/{id}")
    @Operation(summary = "Activate or deactivate a dish",
            description = "Change the activated value of a dish the edited dish details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish edited successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Dish not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Dish> toggleActivated(
            @Valid @PathVariable Long id,
            @Valid @RequestBody ToggleActivationToDishRequest request
    ){
        Dish editedDish = dishHandler.toggleActivation(id, request);
        return new ResponseEntity<>(editedDish, HttpStatus.OK);
    }

}
