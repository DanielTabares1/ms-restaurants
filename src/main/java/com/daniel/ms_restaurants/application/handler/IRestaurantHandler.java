package com.daniel.ms_restaurants.application.handler;

import com.daniel.ms_restaurants.application.dto.CreateRestaurantRequest;
import com.daniel.ms_restaurants.application.dto.RestaurantResponse;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRestaurantHandler {
    Restaurant saveRestaurant(CreateRestaurantRequest request);

    List<RestaurantResponse> getAllRestaurants(int pageNumber, int pageSize);
}
