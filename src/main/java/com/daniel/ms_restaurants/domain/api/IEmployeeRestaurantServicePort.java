package com.daniel.ms_restaurants.domain.api;

import com.daniel.ms_restaurants.domain.model.EmployeeRestaurant;

public interface IEmployeeRestaurantServicePort {
    EmployeeRestaurant saveEmployee(EmployeeRestaurant employeeRestaurant);
    long getRestaurantIdByEmail(String employeeEmail);
}
