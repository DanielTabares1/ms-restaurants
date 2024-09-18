package com.daniel.ms_restaurants.domain.spi;

import com.daniel.ms_restaurants.domain.model.Dish;

public interface IDishPersistencePort {
    Dish createDish(Dish dish);
}
