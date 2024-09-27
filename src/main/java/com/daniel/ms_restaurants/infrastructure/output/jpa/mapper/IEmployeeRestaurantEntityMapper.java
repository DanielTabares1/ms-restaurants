package com.daniel.ms_restaurants.infrastructure.output.jpa.mapper;

import com.daniel.ms_restaurants.domain.model.EmployeeRestaurant;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.EmployeeRestaurantEntity;

public interface IEmployeeRestaurantEntityMapper {
    EmployeeRestaurantEntity toEntity(EmployeeRestaurant employeeRestaurant);
    EmployeeRestaurant toModel(EmployeeRestaurantEntity employeeRestaurantEntity);
}
