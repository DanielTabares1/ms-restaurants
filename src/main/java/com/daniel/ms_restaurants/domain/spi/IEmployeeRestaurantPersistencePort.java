package com.daniel.ms_restaurants.domain.spi;

import com.daniel.ms_restaurants.domain.model.EmployeeRestaurant;

public interface IEmployeeRestaurantPersistencePort {
    EmployeeRestaurant saveEmployee(EmployeeRestaurant employeeRestaurant);
    EmployeeRestaurant getByEmployeeEmail(String employeeEmail);
}
