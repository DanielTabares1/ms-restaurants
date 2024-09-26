package com.daniel.ms_restaurants.domain.spi;

import com.daniel.ms_restaurants.domain.model.Order;

import java.util.List;

public interface IOrderPersistencePort {
    Order createOrder(Order order);

    Order editOrder(long orderId, Order editedOrder);

    Order getById(long orderId);

    List<Order> getByClientId(long clientId);

    List<Order> getByRestaurantIdAndStatus(long restaurantId, String status);
}
