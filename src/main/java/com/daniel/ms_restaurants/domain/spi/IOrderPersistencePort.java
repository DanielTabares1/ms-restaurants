package com.daniel.ms_restaurants.domain.spi;

import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Order;

public interface IOrderPersistencePort {
    Order createOrder(Order order);
    Order appendDish(Dish dish, int amount);
}
