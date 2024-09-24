package com.daniel.ms_restaurants.infrastructure.input.rest;

import com.daniel.ms_restaurants.application.dto.CreateOrderRequest;
import com.daniel.ms_restaurants.application.dto.RestaurantMenuResponse;
import com.daniel.ms_restaurants.application.dto.RestaurantResponse;
import com.daniel.ms_restaurants.application.handler.IDishHandler;
import com.daniel.ms_restaurants.application.handler.IOrderHandler;
import com.daniel.ms_restaurants.application.handler.IRestaurantHandler;
import com.daniel.ms_restaurants.application.mapper.IDishResponseMapper;
import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client")
public class ClientController {

    private final IDishHandler dishHandler;
    private final IRestaurantHandler restaurantHandler;
    private final IDishResponseMapper dishResponseMapper;
    private final IOrderHandler orderHandler;

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


    @GetMapping("/restaurant")
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize
    ) {
        List<RestaurantResponse> restaurants = restaurantHandler.getAllRestaurants(pageNumber, pageSize);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @PostMapping("/order")
    public ResponseEntity<Order> createNewOrder(@RequestBody CreateOrderRequest createOrderRequest){
        Order order = orderHandler.createOrder(createOrderRequest);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

}
