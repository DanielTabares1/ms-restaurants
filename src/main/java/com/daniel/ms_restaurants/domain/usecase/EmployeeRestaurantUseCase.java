package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.application.dto.UserResponse;
import com.daniel.ms_restaurants.domain.api.IEmployeeRestaurantServicePort;
import com.daniel.ms_restaurants.domain.api.IJwtServicePort;
import com.daniel.ms_restaurants.domain.exception.UserNotOwnerOfRestaurantException;
import com.daniel.ms_restaurants.domain.model.EmployeeRestaurant;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.spi.IEmployeeRestaurantPersistencePort;
import com.daniel.ms_restaurants.domain.spi.RestaurantPersistencePort;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtTokenHolder;

public class EmployeeRestaurantUseCase implements IEmployeeRestaurantServicePort {

    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;
    private final RestaurantPersistencePort restaurantPersistencePort;
    private final IJwtServicePort jwtService;
    private final UserFeignClient userFeignClient;

    public EmployeeRestaurantUseCase(IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort, RestaurantPersistencePort restaurantPersistencePort, IJwtServicePort jwtService, UserFeignClient userFeignClient) {
        this.employeeRestaurantPersistencePort = employeeRestaurantPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.jwtService = jwtService;
        this.userFeignClient = userFeignClient;
    }

    @Override
    public EmployeeRestaurant saveEmployee(EmployeeRestaurant employeeRestaurant) {
        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(employeeRestaurant.getRestaurantId());
        if (!userIsOwnerOfRestaurant(restaurant)) {
            throw new UserNotOwnerOfRestaurantException("Authenticated user is not the owner of the restaurant with id: " + employeeRestaurant.getRestaurantId());
        }
        //todo - verify user email is of an employee
        return employeeRestaurantPersistencePort.saveEmployee(employeeRestaurant);
    }

    @Override
    public long getRestaurantIdByEmail(String employeeEmail) {
        return employeeRestaurantPersistencePort.getByEmployeeEmail(employeeEmail).getRestaurantId();
    }

    public boolean userIsOwnerOfRestaurant(Restaurant restaurant) {
        String email = jwtService.extractUsername(JwtTokenHolder.getToken());
        UserResponse userResponse = userFeignClient.findByEmail(email);
        return restaurant.getOwnerId() == userResponse.getId();
    }
}
