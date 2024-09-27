package com.daniel.ms_restaurants.application.mapper;

import com.daniel.ms_restaurants.application.dto.CreateDishRequest;
import com.daniel.ms_restaurants.domain.model.Dish;

public interface IDishRequestMapper {
    Dish toModel(CreateDishRequest dishRequest);
}
