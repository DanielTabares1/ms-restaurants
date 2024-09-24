package com.daniel.ms_restaurants.infrastructure.output.jpa.repository;

import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {
    List<DishEntity> findAllByRestaurantId(long restaurantId);
}
