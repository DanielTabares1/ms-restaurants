package com.daniel.ms_restaurants.domain.spi;

import com.daniel.ms_restaurants.domain.model.Restaurant;

public interface RestaurantPersistencePort {
    Restaurant saveRestaurant(Restaurant restaurant);
    Restaurant getRestaurantById(long id);
}
