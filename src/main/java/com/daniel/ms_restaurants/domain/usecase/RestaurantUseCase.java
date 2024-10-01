package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.domain.model.UserResponse;
import com.daniel.ms_restaurants.domain.api.IRestaurantServicePort;
import com.daniel.ms_restaurants.domain.exception.ErrorMessages;
import com.daniel.ms_restaurants.domain.exception.OwnerNotFoundException;
import com.daniel.ms_restaurants.domain.exception.RestaurantNotFoundException;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.model.enums.UserRoles;
import com.daniel.ms_restaurants.domain.spi.IRestaurantPersistencePort;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;

import java.util.List;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort IRestaurantPersistencePort;
    private final UserFeignClient userFeignClient;

    public RestaurantUseCase(IRestaurantPersistencePort IRestaurantPersistencePort, UserFeignClient userFeignClient) {
        this.IRestaurantPersistencePort = IRestaurantPersistencePort;
        this.userFeignClient = userFeignClient;
    }

    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        UserResponse owner = userFeignClient.adminGetUserById(restaurant.getOwnerId());
        if (owner == null || !owner.getRole().getName().equals(UserRoles.OWNER.toString())) {
            throw new OwnerNotFoundException(ErrorMessages.OWNER_NOT_FOUND.getMessage());
        }
        return IRestaurantPersistencePort.saveRestaurant(restaurant);
    }

    @Override
    public Restaurant getRestaurantById(long id) {
        return IRestaurantPersistencePort.getRestaurantById(id).orElseThrow(
                () -> new RestaurantNotFoundException(ErrorMessages.RESTAURANT_NOT_FOUND.getMessage(id))
        );
    }

    @Override
    public List<Restaurant> getAllRestaurants(int pageNumber, int pageSize) {
        return IRestaurantPersistencePort.getAllRestaurants(pageNumber, pageSize);
    }
}
