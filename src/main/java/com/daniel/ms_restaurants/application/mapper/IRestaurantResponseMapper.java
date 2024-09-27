package com.daniel.ms_restaurants.application.mapper;

import com.daniel.ms_restaurants.application.dto.RestaurantResponse;
import com.daniel.ms_restaurants.domain.model.Restaurant;

public interface IRestaurantResponseMapper {
    RestaurantResponse toResponse(Restaurant restaurant);
}
