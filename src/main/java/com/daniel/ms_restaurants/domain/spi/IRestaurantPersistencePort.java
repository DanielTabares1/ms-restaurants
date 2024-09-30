package com.daniel.ms_restaurants.domain.spi;

import com.daniel.ms_restaurants.domain.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface IRestaurantPersistencePort {
    Restaurant saveRestaurant(Restaurant restaurant);
    Optional<Restaurant> getRestaurantById(long id);
    List<Restaurant> getAllRestaurants(int pageNumber, int pageSize);

}
