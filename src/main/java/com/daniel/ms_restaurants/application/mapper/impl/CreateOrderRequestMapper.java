package com.daniel.ms_restaurants.application.mapper.impl;

import com.daniel.ms_restaurants.application.dto.CreateOrderRequest;
import com.daniel.ms_restaurants.application.mapper.ICreateOrderRequestMapper;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.model.OrderStatus;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;

@Component
public class CreateOrderRequestMapper implements ICreateOrderRequestMapper {
    @Override
    public Order toModel(CreateOrderRequest request) {
        Order order = new Order();
        order.setDate(new Date());
        order.setStatus(OrderStatus.PENDING.toString());
        order.setDishes(new HashSet<>());

        Restaurant restaurant = new Restaurant();
        restaurant.setId(request.getRestaurantId());

        order.setRestaurant(restaurant);

        return order;
    }
}
