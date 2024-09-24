package com.daniel.ms_restaurants.application.handler;

import com.daniel.ms_restaurants.application.dto.CreateRestaurantRequest;
import com.daniel.ms_restaurants.application.dto.RestaurantResponse;
import com.daniel.ms_restaurants.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantHandler {
    Restaurant saveRestaurant(CreateRestaurantRequest request);
    Restaurant getRestaurantById(long restaurantId);
    List<RestaurantResponse> getAllRestaurants(int pageNumber, int pageSize);
}
