package com.daniel.ms_restaurants.domain.spi;

import com.daniel.ms_restaurants.domain.model.Dish;

import java.util.List;

public interface IDishPersistencePort {
    Dish createDish(Dish dish);

    Dish editDish(long dishId, Dish editedDish);

    Dish getDishById(long id);

    List<Dish> getAllDishesByRestaurantId(long restaurantId, int pageNumber, int pageSize);

    List<Dish> getAllDishesByRestaurantIdByCategoryId(long restaurantId, long categoryId, int pageNumber, int pageSize);
}
