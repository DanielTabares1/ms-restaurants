package com.daniel.ms_restaurants.domain.spi;

import com.daniel.ms_restaurants.domain.model.OrderDish;

public interface IOrderDishPersistencePort {
    OrderDish editOrderDish(long id, OrderDish newOrderDish);
}
