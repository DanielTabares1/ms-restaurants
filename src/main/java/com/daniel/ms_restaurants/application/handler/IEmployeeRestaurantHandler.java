package com.daniel.ms_restaurants.application.handler;

import com.daniel.ms_restaurants.domain.model.EmployeeRestaurant;

public interface IEmployeeRestaurantHandler {
    EmployeeRestaurant saveEmployee(EmployeeRestaurant employeeRestaurant);
    long getRestaurantIdByEmployeeEmail(String employeeEmail);
}
