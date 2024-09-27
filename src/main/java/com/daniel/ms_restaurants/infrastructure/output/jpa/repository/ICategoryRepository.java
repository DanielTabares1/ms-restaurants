package com.daniel.ms_restaurants.infrastructure.output.jpa.repository;

import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
