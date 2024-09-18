package com.daniel.ms_restaurants.domain.spi;

import com.daniel.ms_restaurants.domain.model.Dish;

public interface IDishPersistencePort {
    Dish createDish(Dish dish);
    Dish editDish(long dishId, Dish editedDish);
    Dish getDishById(long id);
}
