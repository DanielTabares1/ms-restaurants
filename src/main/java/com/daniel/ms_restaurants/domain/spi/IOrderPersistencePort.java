package com.daniel.ms_restaurants.domain.spi;

import com.daniel.ms_restaurants.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface IOrderPersistencePort {
    Order saveOrder(Order order);

    Order editOrder(long orderId, Order editedOrder);

    Optional<Order> getById(long orderId);

    List<Order> getByClientId(long clientId);

    List<Order> getByRestaurantIdAndStatus(long restaurantId, String status);
}
