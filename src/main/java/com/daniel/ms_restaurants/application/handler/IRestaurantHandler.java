package com.daniel.ms_restaurants.application.handler;

import com.daniel.ms_restaurants.domain.model.Restaurant;

public interface IRestaurantHandler {
    Restaurant saveRestaurant(Restaurant restaurant);
}
