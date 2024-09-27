package com.daniel.ms_restaurants.infrastructure.output.jpa.adapter;

import com.daniel.ms_restaurants.domain.model.OrderDish;
import com.daniel.ms_restaurants.domain.spi.IOrderDishPersistencePort;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.OrderDishEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.IOrderDishEntityMapper;
import com.daniel.ms_restaurants.infrastructure.output.jpa.repository.IOrderDishRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class OrderDishJpaAdapter implements IOrderDishPersistencePort {

    private final IOrderDishRepository orderDishRepository;
    private final IOrderDishEntityMapper orderDishEntityMapper;

    @Override
    public Optional<OrderDish> getOrderDishById(long id, OrderDish newOrderDish) {
        return orderDishRepository.findById(id)
                .map(orderDishEntityMapper::toModel);
    }

    @Override
    public Optional<OrderDish> editOrderDish(long id, OrderDish newOrderDish) {
        OrderDishEntity newOrderDishEntity = orderDishEntityMapper.toEntity(newOrderDish);
        newOrderDishEntity.setId(id);
        OrderDishEntity editedOrderDishEntity = orderDishRepository.save(newOrderDishEntity);
        return Optional.ofNullable(orderDishEntityMapper.toModel(editedOrderDishEntity));
    }
}
