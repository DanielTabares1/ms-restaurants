package com.daniel.ms_restaurants.infrastructure.output.jpa.mapper;

import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.RestaurantEntity;

public interface IRestaurantEntityMapper {
    RestaurantEntity toEntity(Restaurant restaurant);
    Restaurant toModel(RestaurantEntity restaurantEntity);
}
