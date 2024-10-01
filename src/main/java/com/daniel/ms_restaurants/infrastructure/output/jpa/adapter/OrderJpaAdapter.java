package com.daniel.ms_restaurants.infrastructure.output.jpa.adapter;

import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.spi.IOrderPersistencePort;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.OrderEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.IOrderEntityMapper;
import com.daniel.ms_restaurants.infrastructure.output.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;

    @Override
    public Order saveOrder(Order order) {
        OrderEntity orderEntity = orderEntityMapper.toEntity(order);
        OrderEntity newOrder = orderRepository.save(orderEntity);
        return orderEntityMapper.toModel(newOrder);
    }

    @Override
    public Order editOrder(long orderId, Order editedOrder) {
        OrderEntity editedOrderEntity = orderEntityMapper.toEntity(editedOrder);
        editedOrderEntity.setId(orderId);
        OrderEntity newOrderEntity = orderRepository.save(editedOrderEntity);
        return orderEntityMapper.toModel(newOrderEntity);
    }

    @Override
    public Optional<Order> getById(long orderId) {
        return orderRepository.findById(orderId).map(
                orderEntityMapper::toModel
        );
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

    @Override
    public List<Order> getAllByEmployeeIdByStatus(long employeeId, String status) {
        List<OrderEntity> orderEntityList = orderRepository.findByChefIdAndStatus(employeeId, status);
        return orderEntityList.stream().map(
                orderEntityMapper::toModel
        ).toList();
    }
}
