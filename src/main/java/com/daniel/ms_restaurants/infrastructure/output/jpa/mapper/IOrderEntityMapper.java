package com.daniel.ms_restaurants.infrastructure.output.jpa.mapper;

import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.OrderEntity;

public interface IOrderEntityMapper {
    Order toModel(OrderEntity orderEntity);
    OrderEntity toEntity(Order order);
}
