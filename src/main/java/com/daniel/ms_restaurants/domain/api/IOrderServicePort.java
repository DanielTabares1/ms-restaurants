package com.daniel.ms_restaurants.domain.api;

import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Order;

public interface IOrderServicePort {
    Order createOrder(Order order);
    Order appendDish(Dish dish, int amount);
}
