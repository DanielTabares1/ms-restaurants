package com.daniel.ms_restaurants.infrastructure.output.jpa.repository;

import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.EmployeeRestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmployeeRestaurantRepository extends JpaRepository<EmployeeRestaurantEntity, Long> {
    EmployeeRestaurantEntity findByEmployeeEmail(String employeeEmail);
}
