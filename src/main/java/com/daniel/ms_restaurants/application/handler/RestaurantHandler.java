package com.daniel.ms_restaurants.application.handler;

import com.daniel.ms_restaurants.application.dto.RestaurantResponse;
import com.daniel.ms_restaurants.application.exception.OwnerNotFoundException;
import com.daniel.ms_restaurants.application.mapper.IRestaurantResponseMapper;
import com.daniel.ms_restaurants.domain.api.IRestaurantServicePort;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.model.UserResponse;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler{
    private final IRestaurantServicePort restaurantServicePort;
    private final UserFeignClient userFeignClient;
    private final IRestaurantResponseMapper restaurantResponseMapper;

    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        UserResponse owner = userFeignClient.getUserById(restaurant.getOwnerId());

        if (owner == null || !owner.getRole().getName().equals("OWNER")){
            throw new OwnerNotFoundException("Owner was not found or user is not an Owner");
        }
        return restaurantServicePort.saveRestaurant(restaurant);
    }

    @Override
    public List<RestaurantResponse> getAllRestaurants(int pageNumber, int pageSize) {
        return restaurantServicePort.getAllRestaurants(pageNumber, pageSize).stream().map(
                restaurantResponseMapper::toResponse
        ).toList();
    }


}
