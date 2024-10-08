package com.daniel.ms_restaurants.domain.api;

import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Order;

import java.util.List;

public interface IOrderServicePort {
    Order createOrder(Order order);

    Order appendDish(Order order, Dish dish, int amount);

    Order getById(long orderId);

    List<Order> getByClientId(long clientId);

    List<Order> getByRestaurantIdAndByStatus(String status);

    Order assignEmployee(long orderId);

    Order setStatus(long orderId, String status);
}
