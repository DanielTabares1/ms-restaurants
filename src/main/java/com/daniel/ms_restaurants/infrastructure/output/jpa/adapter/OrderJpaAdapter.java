package com.daniel.ms_restaurants.infrastructure.output.jpa.adapter;

import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.spi.IOrderPersistencePort;
import com.daniel.ms_restaurants.infrastructure.exception.OrderNotFoundException;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.OrderEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.IOrderEntityMapper;
import com.daniel.ms_restaurants.infrastructure.output.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
    public Order editOrder(long orderId, Order editedOrder) {
        OrderEntity originalOrder = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("Order not found with id: " + orderId)
        );
        OrderEntity editedOrderEntity = orderEntityMapper.toEntity(editedOrder);
        editedOrderEntity.setId(originalOrder.getId());
        OrderEntity newOrderEntity = orderRepository.save(editedOrderEntity);
        return orderEntityMapper.toModel(newOrderEntity);
    }

    @Override
    public Order getById(long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("Order not found with id: " + orderId)
        );
        return orderEntityMapper.toModel(orderEntity);
    }

    @Override
    public List<Order> getByClientId(long clientId) {
        List<OrderEntity> orderEntities = orderRepository.findByClientId(clientId);
        return orderEntities.stream().map(
                orderEntityMapper::toModel
        ).toList();
    }

    @Override
    public List<Order> getByRestaurantIdAndStatus(long restaurantId, String status) {
        List<OrderEntity> orderEntities = orderRepository.findByRestaurantIdAndStatus(restaurantId, status);
        return orderEntities.stream().map(
                orderEntityMapper::toModel
        ).toList();
    }
}
