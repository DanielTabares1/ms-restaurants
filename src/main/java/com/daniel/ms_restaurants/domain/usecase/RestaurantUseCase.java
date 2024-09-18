package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.domain.api.RestaurantServicePort;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.spi.RestaurantPersistencePort;

public class RestaurantUseCase implements RestaurantServicePort {

    private final RestaurantPersistencePort restaurantPersistencePort;

    public RestaurantUseCase(RestaurantPersistencePort restaurantPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantPersistencePort.saveRestaurant(restaurant);
    }
}
