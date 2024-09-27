package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.application.dto.UserResponse;
import com.daniel.ms_restaurants.domain.api.IRestaurantServicePort;
import com.daniel.ms_restaurants.domain.exception.OwnerNotFoundException;
import com.daniel.ms_restaurants.domain.exception.RestaurantNotFoundException;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.model.enums.UserRoles;
import com.daniel.ms_restaurants.domain.spi.RestaurantPersistencePort;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;

import java.util.List;

public class IRestaurantUseCase implements IRestaurantServicePort {

    private final RestaurantPersistencePort restaurantPersistencePort;
    private final UserFeignClient userFeignClient;

    public IRestaurantUseCase(RestaurantPersistencePort restaurantPersistencePort, UserFeignClient userFeignClient) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userFeignClient = userFeignClient;
    }

    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        UserResponse owner = userFeignClient.getUserById(restaurant.getOwnerId());
        if (owner == null || !owner.getRole().getName().equals(UserRoles.OWNER.toString())) {
            throw new OwnerNotFoundException("Owner was not found or user is not an Owner");
        }
        return restaurantPersistencePort.saveRestaurant(restaurant);
    }

    @Override
    public Restaurant getRestaurantById(long id) {
        return restaurantPersistencePort.getRestaurantById(id).orElseThrow(
                () -> new RestaurantNotFoundException("Restaurant not found with id:" + id)
        );
    }

    @Override
    public List<Restaurant> getAllRestaurants(int pageNumber, int pageSize) {
        return restaurantPersistencePort.getAllRestaurants(pageNumber, pageSize);
    }
}
