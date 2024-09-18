package com.daniel.ms_restaurants.domain.api;

import com.daniel.ms_restaurants.domain.model.Restaurant;

public interface IRestaurantServicePort {
    Restaurant saveRestaurant(Restaurant restaurant);
    Restaurant getRestaurantById(long id);
}
