package com.daniel.ms_restaurants.domain.spi;

import com.daniel.ms_restaurants.domain.model.Restaurant;

import java.util.List;

public interface RestaurantPersistencePort {
    Restaurant saveRestaurant(Restaurant restaurant);
    Restaurant getRestaurantById(long id);
    List<Restaurant> getAllRestaurants(int pageNumber, int pageSize);

}
