package com.daniel.ms_restaurants.application.handler.impl;

import com.daniel.ms_restaurants.application.dto.UserResponse;
import com.daniel.ms_restaurants.application.handler.IEmployeeRestaurantHandler;
import com.daniel.ms_restaurants.domain.api.IEmployeeRestaurantServicePort;
import com.daniel.ms_restaurants.domain.api.IRestaurantServicePort;
import com.daniel.ms_restaurants.domain.model.EmployeeRestaurant;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.infrastructure.exception.UserNotOwnerOfRestaurantException;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtService;
import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtTokenHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class EmployeeRestaurantHandler implements IEmployeeRestaurantHandler {

    private final IEmployeeRestaurantServicePort employeeRestaurantServicePort;
    private final IRestaurantServicePort restaurantServicePort;
    private final JwtService jwtService;
    private final UserFeignClient userFeignClient;


    @Override
    public EmployeeRestaurant saveEmployee(EmployeeRestaurant employeeRestaurant) {
        Restaurant restaurant = restaurantServicePort.getRestaurantById(employeeRestaurant.getRestaurantId());
        if (!userIsOwnerOfRestaurant(restaurant)) {
            throw new UserNotOwnerOfRestaurantException("Authenticated user is not the owner of the restaurant with id: " + employeeRestaurant.getRestaurantId());
        }
        return employeeRestaurantServicePort.saveEmployee(employeeRestaurant);
    }

    @Override
    public long getRestaurantIdByEmployeeEmail(String employeeEmail) {
        return employeeRestaurantServicePort.getRestaurantIdByEmail(employeeEmail);
    }

    public boolean userIsOwnerOfRestaurant(Restaurant restaurant) {
        String email = jwtService.extractUsername(JwtTokenHolder.getToken());
        UserResponse userResponse = userFeignClient.findByEmail(email);
        return restaurant.getOwnerId() == userResponse.getId();
    }
}
