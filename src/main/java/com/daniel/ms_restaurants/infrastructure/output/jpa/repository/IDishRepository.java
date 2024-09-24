package com.daniel.ms_restaurants.infrastructure.output.jpa.repository;

import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.DishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {
    Page<DishEntity> findAllByRestaurantId(long restaurantId, Pageable pageable);
    Page<DishEntity> findAllByRestaurantIdAndCategoryId(long restaurantId, long categoryId, Pageable pageable);
}
