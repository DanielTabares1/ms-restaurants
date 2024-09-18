package com.daniel.ms_restaurants.domain.api;

import com.daniel.ms_restaurants.domain.model.Restaurant;

public interface RestaurantServicePort {
    Restaurant saveRestaurant(Restaurant restaurant);
}
