package com.daniel.ms_restaurants.infrastructure.output.jpa.adapter;

import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.spi.IOrderPersistencePort;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.OrderEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.IOrderEntityMapper;
import com.daniel.ms_restaurants.infrastructure.output.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;

    @Override
    public Order createOrder(Order order) {
        OrderEntity orderEntity = orderEntityMapper.toEntity(order);
        OrderEntity newOrder = orderRepository.save(orderEntity);
        return orderEntityMapper.toModel(newOrder);
    }

    @Override
    public Order appendDish(Dish dish, int amount) {
        return null;
    }
}
