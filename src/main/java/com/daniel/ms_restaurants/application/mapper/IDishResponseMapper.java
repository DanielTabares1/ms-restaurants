package com.daniel.ms_restaurants.application.mapper;

import com.daniel.ms_restaurants.application.dto.DishResponse;
import com.daniel.ms_restaurants.domain.model.Dish;

public interface IDishResponseMapper {
    DishResponse toDishResponse(Dish dish);
}
