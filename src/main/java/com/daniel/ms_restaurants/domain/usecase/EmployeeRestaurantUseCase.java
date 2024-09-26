package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.domain.api.IEmployeeRestaurantServicePort;
import com.daniel.ms_restaurants.domain.model.EmployeeRestaurant;
import com.daniel.ms_restaurants.domain.spi.IEmployeeRestaurantPersistencePort;

public class EmployeeRestaurantUseCase implements IEmployeeRestaurantServicePort {

    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

    public EmployeeRestaurantUseCase(IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort) {
        this.employeeRestaurantPersistencePort = employeeRestaurantPersistencePort;
    }

    @Override
    public EmployeeRestaurant saveEmployee(EmployeeRestaurant employeeRestaurant) {
        //todo - verify user email is of an employee
        return employeeRestaurantPersistencePort.saveEmployee(employeeRestaurant);
    }

    @Override
    public long getRestaurantIdByEmail(String employeeEmail) {
        return employeeRestaurantPersistencePort.getByEmployeeEmail(employeeEmail).getRestaurantId();
    }
}
