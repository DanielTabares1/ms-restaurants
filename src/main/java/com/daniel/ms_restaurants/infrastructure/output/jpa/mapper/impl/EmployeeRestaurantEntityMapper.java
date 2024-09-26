package com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.impl;

import com.daniel.ms_restaurants.domain.model.EmployeeRestaurant;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.EmployeeRestaurantEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.IEmployeeRestaurantEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class EmployeeRestaurantEntityMapper implements IEmployeeRestaurantEntityMapper {
    @Override
    public EmployeeRestaurantEntity toEntity(EmployeeRestaurant employeeRestaurant) {
        return new EmployeeRestaurantEntity(
                employeeRestaurant.getId(),
                employeeRestaurant.getRestaurantId(),
                employeeRestaurant.getEmployeeEmail()
        );
    }

    @Override
    public EmployeeRestaurant toModel(EmployeeRestaurantEntity employeeRestaurantEntity) {
        return new EmployeeRestaurant(
                employeeRestaurantEntity.getId(),
                employeeRestaurantEntity.getRestaurantId(),
                employeeRestaurantEntity.getEmployeeEmail()
        );
    }
}
