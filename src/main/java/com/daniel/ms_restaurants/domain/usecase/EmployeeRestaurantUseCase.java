package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.domain.model.UserResponse;
import com.daniel.ms_restaurants.domain.api.IEmployeeRestaurantServicePort;
import com.daniel.ms_restaurants.domain.api.IJwtServicePort;
import com.daniel.ms_restaurants.domain.exception.ErrorMessages;
import com.daniel.ms_restaurants.domain.exception.RestaurantNotFoundException;
import com.daniel.ms_restaurants.domain.exception.UserNotAnEmployeeException;
import com.daniel.ms_restaurants.domain.exception.UserNotOwnerOfRestaurantException;
import com.daniel.ms_restaurants.domain.model.EmployeeRestaurant;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.model.enums.UserRoles;
import com.daniel.ms_restaurants.domain.spi.IEmployeeRestaurantPersistencePort;
import com.daniel.ms_restaurants.domain.spi.IRestaurantPersistencePort;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtTokenHolder;

import java.util.Objects;

public class EmployeeRestaurantUseCase implements IEmployeeRestaurantServicePort {

    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;
    private final IRestaurantPersistencePort IRestaurantPersistencePort;
    private final IJwtServicePort jwtService;
    private final UserFeignClient userFeignClient;

    public EmployeeRestaurantUseCase(IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort, IRestaurantPersistencePort IRestaurantPersistencePort, IJwtServicePort jwtService, UserFeignClient userFeignClient) {
        this.employeeRestaurantPersistencePort = employeeRestaurantPersistencePort;
        this.IRestaurantPersistencePort = IRestaurantPersistencePort;
        this.jwtService = jwtService;
        this.userFeignClient = userFeignClient;
    }

    @Override
    public EmployeeRestaurant saveEmployee(EmployeeRestaurant employeeRestaurant) {
        Restaurant restaurant = IRestaurantPersistencePort.getRestaurantById(employeeRestaurant.getRestaurantId()).orElseThrow(
                () -> new RestaurantNotFoundException(ErrorMessages.RESTAURANT_NOT_FOUND.getMessage(employeeRestaurant.getRestaurantId()))
        );
        if (!userIsOwnerOfRestaurant(restaurant)) {
            throw new UserNotOwnerOfRestaurantException(ErrorMessages.USER_NOT_OWNER_OF_RESTAURANT.getMessage(restaurant.getId()));
        }
        return employeeRestaurantPersistencePort.saveEmployee(employeeRestaurant);
    }

    @Override
    public long getRestaurantIdByEmail(String employeeEmail) {
        String email = jwtService.extractUsername(JwtTokenHolder.getToken());
        UserResponse userResponse = userFeignClient.getUserByEmail(email);
        if (!Objects.equals(userResponse.getRole().getName(), UserRoles.EMPLOYEE.toString())){
            throw new UserNotAnEmployeeException(ErrorMessages.USER_NOT_AN_EMPLOYEE.getMessage(userResponse.getEmail()));
        }
        return employeeRestaurantPersistencePort.getByEmployeeEmail(employeeEmail).getRestaurantId();
    }

    public boolean userIsOwnerOfRestaurant(Restaurant restaurant) {
        String email = jwtService.extractUsername(JwtTokenHolder.getToken());
        UserResponse userResponse = userFeignClient.findByEmail(email);
        return restaurant.getOwnerId() == userResponse.getId();
    }


}
