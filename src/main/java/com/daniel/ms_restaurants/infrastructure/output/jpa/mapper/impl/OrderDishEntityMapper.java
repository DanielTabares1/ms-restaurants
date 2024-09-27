package com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.impl;

import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.model.OrderDish;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.DishEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.OrderDishEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.OrderEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.IOrderDishEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderDishEntityMapper implements IOrderDishEntityMapper {
    @Override
    public OrderDishEntity toEntity(OrderDish orderDish) {

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderDish.getOrder().getId());

        DishEntity dishEntity = new DishEntity();
        dishEntity.setId(orderDish.getDish().getId());

        return new OrderDishEntity(
                orderDish.getId(),
                orderEntity,
                dishEntity,
                orderDish.getAmount()
        );
    }

    @Override
    public OrderDish toModel(OrderDishEntity orderDishEntity) {

        Order order = new Order();
        order.setId(orderDishEntity.getOrder().getId());

        Dish dish = new Dish();
        dish.setId(orderDishEntity.getDish().getId());


        return new OrderDish(
                orderDishEntity.getId(),
                order,
                dish,
                orderDishEntity.getAmount()
        );
    }
}
