package com.daniel.ms_restaurants.infrastructure.config;

import com.daniel.ms_restaurants.domain.api.RestaurantServicePort;
import com.daniel.ms_restaurants.domain.spi.RestaurantPersistencePort;
import com.daniel.ms_restaurants.domain.usecase.RestaurantUseCase;
import com.daniel.ms_restaurants.infrastructure.output.jpa.adapter.RestaurantJpaAdapter;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.daniel.ms_restaurants.infrastructure.output.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Bean
    public RestaurantPersistencePort restaurantPersistencePort(){
        return new RestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public RestaurantServicePort restaurantServicePort(){
        return new RestaurantUseCase(restaurantPersistencePort());
    }


}
