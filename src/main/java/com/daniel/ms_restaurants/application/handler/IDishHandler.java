package com.daniel.ms_restaurants.application.handler;

import com.daniel.ms_restaurants.application.dto.CreateDishRequest;
import com.daniel.ms_restaurants.application.dto.EditDishRequest;
import com.daniel.ms_restaurants.application.dto.ToggleActivationToDishRequest;
import com.daniel.ms_restaurants.domain.model.Dish;

import java.util.List;

public interface IDishHandler {
    Dish saveDish(CreateDishRequest dishRequest);

    Dish editDish(long dishId, EditDishRequest dishRequest);

    Dish toggleActivation(long dishId, ToggleActivationToDishRequest req);

    List<Dish> getAllDishesByRestaurantId(long restaurantId, int pageNumber, int pageSize);

    List<Dish> getAllDishesByRestaurantIdByCategoryId(long restaurantId, long categoryId, int pageNumber, int pageSize);
}
