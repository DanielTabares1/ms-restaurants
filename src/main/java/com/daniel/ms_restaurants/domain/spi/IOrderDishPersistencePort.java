package com.daniel.ms_restaurants.domain.spi;

import com.daniel.ms_restaurants.domain.model.OrderDish;

import java.util.Optional;

public interface IOrderDishPersistencePort {
    Optional<OrderDish> getOrderDishById(long id, OrderDish newOrderDish);
    Optional<OrderDish> editOrderDish(long id, OrderDish newOrderDish);
}
