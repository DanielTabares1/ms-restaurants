package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.domain.api.IRestaurantServicePort;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.spi.RestaurantPersistencePort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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

    @Override
    public List<Restaurant> getAllRestaurants(int pageNumber, int pageSize) {
        return restaurantPersistencePort.getAllRestaurants(pageNumber, pageSize);
    }
}
