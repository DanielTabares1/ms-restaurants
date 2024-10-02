package com.daniel.ms_restaurants.infrastructure.input.rest;

import com.daniel.ms_restaurants.application.dto.*;
import com.daniel.ms_restaurants.application.handler.IDishHandler;
import com.daniel.ms_restaurants.application.handler.IOrderHandler;
import com.daniel.ms_restaurants.application.handler.IRestaurantHandler;
import com.daniel.ms_restaurants.application.handler.ITraceabilityHandler;
import com.daniel.ms_restaurants.application.mapper.IDishResponseMapper;
import com.daniel.ms_restaurants.application.mapper.IOrderResponseMapper;
import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.model.TraceabilityResponse;
import com.daniel.ms_restaurants.domain.model.enums.OrderStatus;
import com.daniel.ms_restaurants.infrastructure.input.rest.constants.ApiEndpoints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiEndpoints.CLIENT_API)
public class ClientController {

    private final IDishHandler dishHandler;
    private final IRestaurantHandler restaurantHandler;
    private final IDishResponseMapper dishResponseMapper;
    private final IOrderHandler orderHandler;
    private final IOrderResponseMapper orderResponseMapper;
    private final ITraceabilityHandler traceabilityHandler;

    @Operation(summary = "Get dishes by restaurant ID",
            description = "Returns a list of dishes for a specific restaurant by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dishes retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/dishes/{restaurantId}")
    public RestaurantMenuResponse getAllDishesByRestaurantId(
            @PathVariable long restaurantId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize
    ) {
        List<Dish> dishes = dishHandler.getAllDishesByRestaurantId(restaurantId, pageNumber, pageSize);
        Restaurant restaurant = restaurantHandler.getRestaurantById(restaurantId);
        return new RestaurantMenuResponse(restaurant, dishes.stream().map(
                dishResponseMapper::toDishResponse
        ).toList());
    }

    @Operation(summary = "Get dishes by restaurant and category",
            description = "Returns a list of dishes for a specific restaurant and category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dishes retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant or category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/dishes/by-restaurant/by-category")
    public RestaurantMenuResponse getAllDishesByRestaurantIdByCategoryId(
            @RequestParam(value = "restaurantId", defaultValue = "1", required = false) long restaurantId,
            @RequestParam(value = "categoryId", defaultValue = "1", required = false) long categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize
    ) {
        List<Dish> dishes = dishHandler.getAllDishesByRestaurantIdByCategoryId(restaurantId, categoryId, pageNumber, pageSize);
        Restaurant restaurant = restaurantHandler.getRestaurantById(restaurantId);
        return new RestaurantMenuResponse(restaurant, dishes.stream().map(
                dishResponseMapper::toDishResponse
        ).toList());
    }

    @Operation(summary = "Get all restaurants",
            description = "Returns a paginated list of all restaurants.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurants retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/restaurant")
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize
    ) {
        List<RestaurantResponse> restaurants = restaurantHandler.getAllRestaurants(pageNumber, pageSize);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @Operation(summary = "Create a new order",
            description = "Creates a new order and returns the created order details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/order")
    public ResponseEntity<OrderResponse> createNewOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        Order order = orderHandler.createOrder(createOrderRequest);
        OrderResponse orderResponse = orderResponseMapper.toResponse(order);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Append dish to order",
            description = "Appends a dish to an existing order and returns the updated order details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish added to order successfully"),
            @ApiResponse(responseCode = "404", description = "Order or dish not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/order/append-dish")
    public ResponseEntity<OrderResponse> appendDishToOrder(
            @RequestParam int orderId,
            @RequestParam int dishId,
            @RequestParam int amount
    ) {
        Order order = orderHandler.appendDish(orderId, dishId, amount);
        OrderResponse orderResponse = orderResponseMapper.toResponse(order);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @Operation(summary = "Cancel an order",
            description = "Cancels an existing order and returns the updated order details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/order/cancel/{orderId}")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable long orderId) {
        Order order = orderHandler.getById(orderId);
        Order editedOrder = orderHandler.cancellOrder(order);
        return new ResponseEntity<>(orderResponseMapper.toResponse(editedOrder), HttpStatus.OK);
    }

    @Operation(summary = "Get traceability by order ID",
            description = "Returns the traceability information for a specific order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Traceability retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/traceability/{orderId}")
    public ResponseEntity<List<TraceabilityResponse>> getByOrderId(@PathVariable long orderId) {
        List<TraceabilityResponse> traceabilityResponseList = traceabilityHandler.getTraceabilityByOrderId(orderId);
        return new ResponseEntity<>(traceabilityResponseList, HttpStatus.OK);
    }

}
