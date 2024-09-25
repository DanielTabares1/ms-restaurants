package com.daniel.ms_restaurants.infrastructure.output.jpa.adapter;

import com.daniel.ms_restaurants.domain.model.OrderDish;
import com.daniel.ms_restaurants.domain.spi.IOrderDishPersistencePort;
import com.daniel.ms_restaurants.infrastructure.exception.OrderDishNotFound;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.OrderDishEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.IOrderDishEntityMapper;
import com.daniel.ms_restaurants.infrastructure.output.jpa.repository.IOrderDishRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderDishJpaAdapter implements IOrderDishPersistencePort {

    private final IOrderDishRepository orderDishRepository;
    private final IOrderDishEntityMapper orderDishEntityMapper;

    @Override
    public OrderDish editOrderDish(long id, OrderDish newOrderDish) {
        OrderDishEntity orderDishEntity = orderDishRepository.findById(id).orElseThrow(
                () -> new OrderDishNotFound("OrderDish not found with id: " + id)
        );

        OrderDishEntity newOrderDishEntity = orderDishEntityMapper.toEntity(newOrderDish);
        newOrderDishEntity.setId(id);

        OrderDishEntity editedOrderDishEntity = orderDishRepository.save(newOrderDishEntity);

        return orderDishEntityMapper.toModel(editedOrderDishEntity);
    }
}
