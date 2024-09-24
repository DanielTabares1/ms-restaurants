package com.daniel.ms_restaurants.application.mapper.impl;

import com.daniel.ms_restaurants.application.dto.CreateOrderRequest;
import com.daniel.ms_restaurants.application.mapper.ICreateOrderRequestMapper;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;

@Component
public class CreateOrderRequestMapper implements ICreateOrderRequestMapper {
    @Override
    public Order toModel(CreateOrderRequest request) {
        return new Order(
                request.getClientId(),
                new Date(),
                OrderStatus.PENDING.toString(),
                request.getChefId(),
                new HashSet<>(),
               null
        );
    }
}
