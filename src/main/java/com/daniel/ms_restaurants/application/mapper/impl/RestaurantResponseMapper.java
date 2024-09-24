package com.daniel.ms_restaurants.application.mapper.impl;

import com.daniel.ms_restaurants.application.dto.RestaurantResponse;
import com.daniel.ms_restaurants.application.mapper.IRestaurantResponseMapper;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class RestaurantResponseMapper implements IRestaurantResponseMapper {
    @Override
    public RestaurantResponse toResponse(Restaurant restaurant) {
        if (restaurant == null){
            return null;
        }

        return new RestaurantResponse(restaurant.getName(), restaurant.getLogoUrl());

    }
}
