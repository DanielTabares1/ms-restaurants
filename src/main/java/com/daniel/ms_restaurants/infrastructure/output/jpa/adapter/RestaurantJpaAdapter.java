package com.daniel.ms_restaurants.infrastructure.output.jpa.adapter;

import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.spi.RestaurantPersistencePort;
import com.daniel.ms_restaurants.infrastructure.exception.RestaurantNotFoundException;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.RestaurantEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.daniel.ms_restaurants.infrastructure.output.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements RestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = restaurantEntityMapper.toEntity(restaurant);
        RestaurantEntity newRestaurant = restaurantRepository.save(restaurantEntity);
        return restaurantEntityMapper.toModel(newRestaurant);
    }

    @Override
    public Restaurant getRestaurantById(long id) {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(id).orElseThrow(
                () -> new RestaurantNotFoundException("Restaurant not found with id " + id)
        );
        return restaurantEntityMapper.toModel(restaurantEntity);
    }
}
