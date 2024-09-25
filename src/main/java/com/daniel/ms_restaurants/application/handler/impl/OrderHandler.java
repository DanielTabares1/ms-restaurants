package com.daniel.ms_restaurants.application.handler.impl;

import com.daniel.ms_restaurants.application.dto.CreateOrderRequest;
import com.daniel.ms_restaurants.application.exception.OrderAndDishNotBelongToTheSameRestaurant;
import com.daniel.ms_restaurants.application.handler.IOrderHandler;
import com.daniel.ms_restaurants.application.mapper.ICreateOrderRequestMapper;
import com.daniel.ms_restaurants.domain.api.IDishServicePort;
import com.daniel.ms_restaurants.domain.api.IOrderServicePort;
import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.model.UserResponse;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtService;
import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtTokenHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final ICreateOrderRequestMapper orderRequestMapper;
    private final UserFeignClient userFeignClient;
    private final JwtService jwtService;
    private final IDishServicePort dishServicePort;


    @Override
    public Order createOrder(CreateOrderRequest createOrderRequest) {
        UserResponse client = userFeignClient.findByEmail(jwtService.extractUsername(JwtTokenHolder.getToken()));
        Order order = orderRequestMapper.toModel(createOrderRequest);
        order.setClientId(client.getId());
        return orderServicePort.createOrder(order);
    }

    @Override
    public Order appendDish(long orderId, long dishId, int amount) {
        Order order = orderServicePort.getById(orderId);
        Dish dish = dishServicePort.getDishById(dishId);

        if(order.getRestaurant().getId() != dish.getRestaurant().getId()){
            throw new OrderAndDishNotBelongToTheSameRestaurant("The order and the dish not belongs to the same restaurant");
        }

        orderServicePort.appendDish(order, dish, amount);
        return order;
    }


}
