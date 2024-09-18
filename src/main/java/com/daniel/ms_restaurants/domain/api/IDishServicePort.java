package com.daniel.ms_restaurants.domain.api;

import com.daniel.ms_restaurants.domain.model.Dish;

public interface IDishServicePort {
    Dish createDish(Dish dish);
    Dish editDish(long dishId, Dish editedDish);
    Dish getDishById(long id);
}
