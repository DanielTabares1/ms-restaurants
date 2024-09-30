package com.daniel.ms_restaurants.application.handler;

import com.daniel.ms_restaurants.application.dto.CreateOrderRequest;
import com.daniel.ms_restaurants.domain.model.Order;

import java.util.List;

public interface IOrderHandler {
    Order createOrder(CreateOrderRequest createOrderRequest);
    Order appendDish(long orderId, long dishId, int amount);
    List<Order> getByClientId(long clientId);
    List<Order> getByStatus(String status);
    Order assignEmployee(long orderId);
    Order editStatus(long orderId, String statusName);
    Order getById(long id);
    Order deliveryOrder(Order order, String code);
}
