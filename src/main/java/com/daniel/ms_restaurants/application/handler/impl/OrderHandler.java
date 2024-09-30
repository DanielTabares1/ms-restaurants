package com.daniel.ms_restaurants.application.handler.impl;

import com.daniel.ms_restaurants.application.dto.CreateOrderRequest;
import com.daniel.ms_restaurants.application.handler.IOrderHandler;
import com.daniel.ms_restaurants.application.mapper.ICreateOrderRequestMapper;
import com.daniel.ms_restaurants.domain.api.IDishServicePort;
import com.daniel.ms_restaurants.domain.api.IOrderServicePort;
import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final ICreateOrderRequestMapper orderRequestMapper;
    private final IDishServicePort dishServicePort;


    @Override
    public Order createOrder(CreateOrderRequest createOrderRequest) {
        Order order = orderRequestMapper.toModel(createOrderRequest);
        return orderServicePort.createOrder(order);
    }

    @Override
    public Order appendDish(long orderId, long dishId, int amount) {
        Order order = orderServicePort.getById(orderId);
        Dish dish = dishServicePort.getDishById(dishId);
        orderServicePort.appendDish(order, dish, amount);
        return order;
    }

    @Override
    public List<Order> getByClientId(long clientId) {
        return orderServicePort.getByClientId(clientId);
    }

    @Override
    public List<Order> getByStatus(String status) {
        return orderServicePort.getByRestaurantIdAndByStatus(status);
    }

    @Override
    public Order assignEmployee(long orderId) {
        return orderServicePort.assignEmployee(orderId);
    }

    @Override
    public Order editStatus(long orderId, String statusName) {
        return orderServicePort.setStatus(orderId, statusName);
    }

    @Override
    public Order getById(long id) {
        return orderServicePort.getById(id);
    }

    @Override
    public Order deliveryOrder(Order order, String code) {
        return orderServicePort.deliverOrder(order, code);
    }


}
