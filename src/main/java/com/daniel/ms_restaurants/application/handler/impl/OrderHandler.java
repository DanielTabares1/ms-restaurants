package com.daniel.ms_restaurants.application.handler.impl;

import com.daniel.ms_restaurants.application.dto.CreateOrderRequest;
import com.daniel.ms_restaurants.application.exception.OrderAndDishNotBelongToTheSameRestaurant;
import com.daniel.ms_restaurants.application.exception.OrderNotBelongToClientException;
import com.daniel.ms_restaurants.application.exception.UserAlreadyHaveAnOrderActive;
import com.daniel.ms_restaurants.application.handler.IOrderHandler;
import com.daniel.ms_restaurants.application.mapper.ICreateOrderRequestMapper;
import com.daniel.ms_restaurants.domain.api.IDishServicePort;
import com.daniel.ms_restaurants.domain.api.IOrderServicePort;
import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.model.OrderStatus;
import com.daniel.ms_restaurants.domain.model.UserResponse;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtService;
import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtTokenHolder;
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
    private final UserFeignClient userFeignClient;
    private final JwtService jwtService;
    private final IDishServicePort dishServicePort;


    @Override
    public Order createOrder(CreateOrderRequest createOrderRequest) {
        UserResponse client = userFeignClient.findByEmail(jwtService.extractUsername(JwtTokenHolder.getToken()));

        List<Order> ordersOfClient = orderServicePort.getByClientId(client.getId());
        for (Order order : ordersOfClient) {
            if (order.getState().equals(OrderStatus.PENDING.toString()) ||
                    order.getState().equals(OrderStatus.IN_PROGRESS.toString()) ||
                    order.getState().equals(OrderStatus.READY.toString())
            ) {
                throw new UserAlreadyHaveAnOrderActive("The client already has an order in progress");
            }
        }

        Order order = orderRequestMapper.toModel(createOrderRequest);
        order.setClientId(client.getId());
        return orderServicePort.createOrder(order);
    }

    @Override
    public Order appendDish(long orderId, long dishId, int amount) {
        Order order = orderServicePort.getById(orderId);

        UserResponse client = userFeignClient.findByEmail(jwtService.extractUsername(JwtTokenHolder.getToken()));
        if (client.getId() != order.getClientId()) {
            throw new OrderNotBelongToClientException("Order with id: " + orderId + "not belongs to client with email: " + client.getUsername());
        }

        Dish dish = dishServicePort.getDishById(dishId);

        if (order.getRestaurant().getId() != dish.getRestaurant().getId()) {
            throw new OrderAndDishNotBelongToTheSameRestaurant("The order and the dish not belongs to the same restaurant");
        }

        orderServicePort.appendDish(order, dish, amount);
        return order;
    }

    @Override
    public List<Order> getByClientId(long clientId) {
        return orderServicePort.getByClientId(clientId);
    }


}
