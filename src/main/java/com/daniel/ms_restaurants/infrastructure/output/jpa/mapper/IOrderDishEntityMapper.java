package com.daniel.ms_restaurants.infrastructure.output.jpa.mapper;

import com.daniel.ms_restaurants.domain.model.OrderDish;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.OrderDishEntity;

public interface IOrderDishEntityMapper {
    OrderDishEntity toEntity(OrderDish orderDish);
    OrderDish toModel(OrderDishEntity orderDishEntity);
}
