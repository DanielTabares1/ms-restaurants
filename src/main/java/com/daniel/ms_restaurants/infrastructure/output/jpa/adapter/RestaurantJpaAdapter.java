package com.daniel.ms_restaurants.infrastructure.output.jpa.adapter;

import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.spi.RestaurantPersistencePort;
import com.daniel.ms_restaurants.infrastructure.exception.RestaurantNotFoundException;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.RestaurantEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.daniel.ms_restaurants.infrastructure.output.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

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

    @Override
    public List<Restaurant> getAllRestaurants(int pageNumber, int pageSize) {
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Restaurant> restaurants = restaurantRepository.findAll(pageable)
                .map(restaurantEntityMapper::toModel);

       return restaurants.stream().toList();
    }
}
