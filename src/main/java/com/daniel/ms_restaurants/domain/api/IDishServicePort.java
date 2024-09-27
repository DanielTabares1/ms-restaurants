package com.daniel.ms_restaurants.domain.api;

import com.daniel.ms_restaurants.domain.model.Dish;

import java.util.List;

public interface IDishServicePort {
    Dish createDish(Dish dish);

    Dish editDish(long dishId, Dish editedDish);

    Dish getDishById(long id);

    List<Dish> findAllDishesByRestaurantId(long restaurantId, int pageNumber, int pageSize);

    List<Dish> findAllDishesByRestaurantIdByCategoryId(long restaurantId, long categoryId, int pageNumber, int pageSize);
}
