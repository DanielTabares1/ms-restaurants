package com.daniel.ms_restaurants.domain.api;

import com.daniel.ms_restaurants.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRestaurantServicePort {
    Restaurant saveRestaurant(Restaurant restaurant);
    Restaurant getRestaurantById(long id);
    List<Restaurant> getAllRestaurants(int pageNumber, int pageSize);
}
