package com.daniel.ms_restaurants.application.mapper.impl;

import com.daniel.ms_restaurants.application.dto.CreateRestaurantRequest;
import com.daniel.ms_restaurants.application.mapper.ICreateRestaurantRequestMapper;
import com.daniel.ms_restaurants.domain.model.Restaurant;


public class CreateRestaurantRequestMapper implements ICreateRestaurantRequestMapper {
    @Override
    public Restaurant toModel(CreateRestaurantRequest request) {
        if (request == null){
            return null;
        }

        return new Restaurant(
                request.getName(),
                request.getAddress(),
                request.getOwnerId(),
                request.getPhoneNumber(),
                request.getLogoUrl(),
                request.getNit()
        );
    }
}
