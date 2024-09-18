package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.domain.api.IRestaurantServicePort;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.spi.RestaurantPersistencePort;
import org.aspectj.apache.bcel.generic.RET;

public class IRestaurantUseCase implements IRestaurantServicePort {

    private final RestaurantPersistencePort restaurantPersistencePort;

    public IRestaurantUseCase(RestaurantPersistencePort restaurantPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantPersistencePort.saveRestaurant(restaurant);
    }

    @Override
    public Restaurant getRestaurantById(long id) {
        return restaurantPersistencePort.getRestaurantById(id);
    }
}
