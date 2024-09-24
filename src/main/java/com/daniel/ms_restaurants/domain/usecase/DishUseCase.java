package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.domain.api.IDishServicePort;
import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.spi.IDishPersistencePort;

import java.util.List;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
    }

    @Override
    public Dish createDish(Dish dish) {
        return dishPersistencePort.createDish(dish);
    }

    @Override
    public Dish getDishById(long id) {
        return dishPersistencePort.getDishById(id);
    }

    @Override
    public List<Dish> findAllDishesByRestaurantId(long restaurantId) {
        return dishPersistencePort.getAllDishesByRestaurantId(restaurantId);
    }

    @Override
    public Dish editDish(long dishId, Dish editedDish) {
        return dishPersistencePort.editDish(dishId, editedDish);
    }


}
