package com.daniel.ms_restaurants.infrastructure.input.rest;

import com.daniel.ms_restaurants.application.dto.RestaurantMenuResponse;
import com.daniel.ms_restaurants.application.dto.RestaurantResponse;
import com.daniel.ms_restaurants.application.handler.IDishHandler;
import com.daniel.ms_restaurants.application.handler.IRestaurantHandler;
import com.daniel.ms_restaurants.application.mapper.IDishResponseMapper;
import com.daniel.ms_restaurants.domain.model.Dish;
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

    @GetMapping("/dishes/{restaurantId}")
    public RestaurantMenuResponse getAllDishesByRestaurantId(@PathVariable long restaurantId){
        List<Dish> dishes = dishHandler.getAllDishesByRestaurantId(restaurantId);
        Restaurant restaurant = restaurantHandler.getRestaurantById(restaurantId);
        return new RestaurantMenuResponse(restaurant, dishes.stream().map(
                dishResponseMapper::toDishResponse
        ).toList());
    }

    @GetMapping("/client/restaurant")
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize
    ){
        List<RestaurantResponse> restaurants = restaurantHandler.getAllRestaurants(pageNumber, pageSize);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

}
