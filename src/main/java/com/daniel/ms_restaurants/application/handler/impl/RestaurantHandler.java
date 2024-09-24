package com.daniel.ms_restaurants.application.handler.impl;

import com.daniel.ms_restaurants.application.dto.CreateRestaurantRequest;
import com.daniel.ms_restaurants.application.dto.RestaurantResponse;
import com.daniel.ms_restaurants.application.exception.OwnerNotFoundException;
import com.daniel.ms_restaurants.application.handler.IRestaurantHandler;
import com.daniel.ms_restaurants.application.mapper.ICreateRestaurantRequestMapper;
import com.daniel.ms_restaurants.application.mapper.IRestaurantResponseMapper;
import com.daniel.ms_restaurants.domain.api.IRestaurantServicePort;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.model.UserResponse;
import com.daniel.ms_restaurants.domain.model.UserRoles;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {
    private final IRestaurantServicePort restaurantServicePort;
    private final UserFeignClient userFeignClient;
    private final IRestaurantResponseMapper restaurantResponseMapper;
    private final ICreateRestaurantRequestMapper restaurantRequestMapper;

    @Override
    public Restaurant saveRestaurant(CreateRestaurantRequest request) {
        UserResponse owner = userFeignClient.getUserById(request.getOwnerId());
        if (owner == null || !owner.getRole().getName().equals(UserRoles.OWNER.toString())) {
            throw new OwnerNotFoundException("Owner was not found or user is not an Owner");
        }
        Restaurant restaurant = restaurantRequestMapper.toModel(request);
        return restaurantServicePort.saveRestaurant(restaurant);
    }

    @Override
    public Restaurant getRestaurantById(long restaurantId) {
        return restaurantServicePort.getRestaurantById(restaurantId);
    }

    @Override
    public List<RestaurantResponse> getAllRestaurants(int pageNumber, int pageSize) {
        return restaurantServicePort.getAllRestaurants(pageNumber, pageSize).stream().map(
                restaurantResponseMapper::toResponse
        ).toList();
    }
}
