package com.daniel.ms_restaurants.application.handler.impl;

import com.daniel.ms_restaurants.application.dto.CreateOrderRequest;
import com.daniel.ms_restaurants.application.handler.IOrderHandler;
import com.daniel.ms_restaurants.application.mapper.ICreateOrderRequestMapper;
import com.daniel.ms_restaurants.domain.api.IOrderServicePort;
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


    @Override
    public Order createOrder(CreateOrderRequest createOrderRequest) {
        UserResponse client = userFeignClient.findByEmail(jwtService.extractUsername(JwtTokenHolder.getToken()));
        Order order = orderRequestMapper.toModel(createOrderRequest);
        order.setClientId(client.getId());
        return orderServicePort.createOrder(order);
    }
}
