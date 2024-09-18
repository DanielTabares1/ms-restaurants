package com.daniel.ms_restaurants.application.handler;

import com.daniel.ms_restaurants.application.dto.CreateDishRequest;
import com.daniel.ms_restaurants.application.dto.EditDishRequest;
import com.daniel.ms_restaurants.domain.model.Dish;

public interface IDishHandler {
    Dish saveDish(CreateDishRequest dishRequest);
    Dish editDish(long dishId, EditDishRequest dishRequest);
}
