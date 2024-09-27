package com.daniel.ms_restaurants.application.handler.impl;

import com.daniel.ms_restaurants.application.handler.IEmployeeRestaurantHandler;
import com.daniel.ms_restaurants.domain.api.IEmployeeRestaurantServicePort;
import com.daniel.ms_restaurants.domain.api.IRestaurantServicePort;
import com.daniel.ms_restaurants.domain.model.EmployeeRestaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class EmployeeRestaurantHandler implements IEmployeeRestaurantHandler {

    private final IEmployeeRestaurantServicePort employeeRestaurantServicePort;
    private final IRestaurantServicePort restaurantServicePort;


    @Override
    public EmployeeRestaurant saveEmployee(EmployeeRestaurant employeeRestaurant) {
        return employeeRestaurantServicePort.saveEmployee(employeeRestaurant);
    }

    @Override
    public long getRestaurantIdByEmployeeEmail(String employeeEmail) {
        return employeeRestaurantServicePort.getRestaurantIdByEmail(employeeEmail);
    }

}
