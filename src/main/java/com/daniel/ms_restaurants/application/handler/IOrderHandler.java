package com.daniel.ms_restaurants.application.handler;

import com.daniel.ms_restaurants.application.dto.CreateOrderRequest;
import com.daniel.ms_restaurants.domain.model.Order;

public interface IOrderHandler {
    Order createOrder(CreateOrderRequest createOrderRequest);
    Order appendDish(long orderId, long dishId, int amount);
}
