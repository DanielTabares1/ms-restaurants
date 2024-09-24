package com.daniel.ms_restaurants.application.mapper;

import com.daniel.ms_restaurants.application.dto.CreateRestaurantRequest;
import com.daniel.ms_restaurants.domain.model.Restaurant;

public interface ICreateRestaurantRequestMapper {

    Restaurant toModel(CreateRestaurantRequest request);

}
