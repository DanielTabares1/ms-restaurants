package com.daniel.ms_restaurants.infrastructure.output.jpa.mapper;

import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.DishEntity;

public interface IDishEntityMapper {
    Dish toModel(DishEntity dishEntity);
    DishEntity toEntity(Dish dish);
}
