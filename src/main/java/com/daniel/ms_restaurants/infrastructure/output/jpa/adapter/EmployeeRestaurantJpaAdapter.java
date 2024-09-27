package com.daniel.ms_restaurants.infrastructure.output.jpa.adapter;

import com.daniel.ms_restaurants.domain.model.EmployeeRestaurant;
import com.daniel.ms_restaurants.domain.spi.IEmployeeRestaurantPersistencePort;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.EmployeeRestaurantEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.IEmployeeRestaurantEntityMapper;
import com.daniel.ms_restaurants.infrastructure.output.jpa.repository.IEmployeeRestaurantRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmployeeRestaurantJpaAdapter implements IEmployeeRestaurantPersistencePort {

    private final IEmployeeRestaurantRepository employeeRestaurantRepository;
    private final IEmployeeRestaurantEntityMapper employeeRestaurantEntityMapper;

    @Override
    public EmployeeRestaurant saveEmployee(EmployeeRestaurant employeeRestaurant) {
        EmployeeRestaurantEntity employeeRestaurantEntity = employeeRestaurantEntityMapper.toEntity(employeeRestaurant);
        EmployeeRestaurantEntity savedEmployeeRestaurantEntity = employeeRestaurantRepository.save(employeeRestaurantEntity);
        return employeeRestaurantEntityMapper.toModel(savedEmployeeRestaurantEntity);
    }

    @Override
    public EmployeeRestaurant getByEmployeeEmail(String employeeEmail) {
        return employeeRestaurantEntityMapper.toModel(
                employeeRestaurantRepository.findByEmployeeEmail(employeeEmail));
    }
}
