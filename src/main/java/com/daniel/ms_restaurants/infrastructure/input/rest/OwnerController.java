package com.daniel.ms_restaurants.infrastructure.input.rest;

import com.daniel.ms_restaurants.application.dto.CreateDishRequest;
import com.daniel.ms_restaurants.application.dto.EditDishRequest;
import com.daniel.ms_restaurants.application.dto.ToggleActivationToDishRequest;
import com.daniel.ms_restaurants.application.handler.IDishHandler;
import com.daniel.ms_restaurants.application.handler.IEmployeeRestaurantHandler;
import com.daniel.ms_restaurants.application.handler.IOrderHandler;
import com.daniel.ms_restaurants.application.handler.ITraceabilityHandler;
import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.EmployeeRestaurant;
import com.daniel.ms_restaurants.infrastructure.input.rest.constants.ApiEndpoints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoints.OWNER_API)
@RequiredArgsConstructor
public class OwnerController {

    private final IDishHandler dishHandler;
    private final IEmployeeRestaurantHandler employeeRestaurantHandler;
    private final ITraceabilityHandler traceabilityHandler;
    private final IOrderHandler orderHandler;

    @Operation(summary = "Assign an employee to a restaurant",
            description = "Assigns an employee to a specific restaurant and returns the details of the assignment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee assigned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/employee")
    public ResponseEntity<EmployeeRestaurant> assignEmployee(@RequestBody EmployeeRestaurant employeeRestaurant) {
        EmployeeRestaurant savedEmployeeRestaurant = employeeRestaurantHandler.saveEmployee(employeeRestaurant);
        return new ResponseEntity<>(savedEmployeeRestaurant, HttpStatus.CREATED);
    }

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
    ) {
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
    ) {
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
    ) {
        Dish editedDish = dishHandler.toggleActivation(id, request);
        return new ResponseEntity<>(editedDish, HttpStatus.OK);
    }

    @Operation(summary = "Get order efficiency",
            description = "Returns the formatted efficiency of a specific order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Efficiency retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/order/get-efficiency/{orderId}")
    public ResponseEntity<String> getEfficiency(@PathVariable long orderId) {
        return new ResponseEntity<>(traceabilityHandler.getFormattedEfficiency(orderId), HttpStatus.OK);
    }

    @Operation(summary = "Get efficiency by employee",
            description = "Returns the formatted efficiency of an employee based on their orders.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Efficiency retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/order/get-efficiency/by-employee-id/{employeeId}")
    public ResponseEntity<String> getEfficiencyByEmployee(@PathVariable long employeeId){
        return new ResponseEntity<>(orderHandler.getFormattedEfficiencyOfEmployee(employeeId), HttpStatus.OK);
    }

}
