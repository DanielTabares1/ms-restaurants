package com.daniel.ms_restaurants.infrastructure.output.jpa.repository;

import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByClientId(long clientId);
    List<OrderEntity> findByRestaurantIdAndStatus(long restaurantId, String status);
}
