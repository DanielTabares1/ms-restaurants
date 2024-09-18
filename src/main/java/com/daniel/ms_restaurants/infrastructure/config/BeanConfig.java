package com.daniel.ms_restaurants.infrastructure.config;

import com.daniel.ms_restaurants.domain.api.ICategoryServicePort;
import com.daniel.ms_restaurants.domain.api.IDishServicePort;
import com.daniel.ms_restaurants.domain.api.IRestaurantServicePort;
import com.daniel.ms_restaurants.domain.spi.ICategoryPersistencePort;
import com.daniel.ms_restaurants.domain.spi.IDishPersistencePort;
import com.daniel.ms_restaurants.domain.spi.RestaurantPersistencePort;
import com.daniel.ms_restaurants.domain.usecase.CategoryUseCase;
import com.daniel.ms_restaurants.domain.usecase.DishUseCase;
import com.daniel.ms_restaurants.domain.usecase.IRestaurantUseCase;
import com.daniel.ms_restaurants.infrastructure.output.jpa.adapter.CategoryJpaAdapter;
import com.daniel.ms_restaurants.infrastructure.output.jpa.adapter.DishJpaAdapter;
import com.daniel.ms_restaurants.infrastructure.output.jpa.adapter.RestaurantJpaAdapter;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.ICategoryEntityMapper;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.IDishEntityMapper;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.daniel.ms_restaurants.infrastructure.output.jpa.repository.ICategoryRepository;
import com.daniel.ms_restaurants.infrastructure.output.jpa.repository.IDishRepository;
import com.daniel.ms_restaurants.infrastructure.output.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;
    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;

    @Bean
    public RestaurantPersistencePort restaurantPersistencePort(){
        return new RestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort(){
        return new IRestaurantUseCase(restaurantPersistencePort());
    }


    @Bean
    public IDishPersistencePort dishPersistencePort(){
        return new DishJpaAdapter(dishRepository, dishEntityMapper);
    }

    @Bean
    public IDishServicePort dishServicePort(){
        return new DishUseCase(dishPersistencePort());
    }

    @Bean
    public ICategoryPersistencePort categoryPersistencePort(){
        return new CategoryJpaAdapter(categoryRepository, categoryEntityMapper);
    }

    @Bean
    public ICategoryServicePort categoryServicePort(){
        return new CategoryUseCase(categoryPersistencePort());
    }


}
