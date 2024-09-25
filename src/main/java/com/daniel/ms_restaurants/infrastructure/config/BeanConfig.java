package com.daniel.ms_restaurants.infrastructure.config;

import com.daniel.ms_restaurants.domain.api.ICategoryServicePort;
import com.daniel.ms_restaurants.domain.api.IDishServicePort;
import com.daniel.ms_restaurants.domain.api.IOrderServicePort;
import com.daniel.ms_restaurants.domain.api.IRestaurantServicePort;
import com.daniel.ms_restaurants.domain.spi.*;
import com.daniel.ms_restaurants.domain.usecase.CategoryUseCase;
import com.daniel.ms_restaurants.domain.usecase.DishUseCase;
import com.daniel.ms_restaurants.domain.usecase.IRestaurantUseCase;
import com.daniel.ms_restaurants.domain.usecase.OrderUseCase;
import com.daniel.ms_restaurants.infrastructure.output.jpa.adapter.*;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.*;
import com.daniel.ms_restaurants.infrastructure.output.jpa.repository.*;
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

    private final IOrderEntityMapper orderEntityMapper;
    private final IOrderRepository orderRepository;

    private final IOrderDishEntityMapper orderDishEntityMapper;
    private final IOrderDishRepository orderDishRepository;


    @Bean
    public RestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort() {
        return new IRestaurantUseCase(restaurantPersistencePort());
    }


    @Bean
    public IDishPersistencePort dishPersistencePort() {
        return new DishJpaAdapter(dishRepository, dishEntityMapper);
    }

    @Bean
    public IDishServicePort dishServicePort() {
        return new DishUseCase(dishPersistencePort());
    }

    @Bean
    public ICategoryPersistencePort categoryPersistencePort() {
        return new CategoryJpaAdapter(categoryRepository, categoryEntityMapper);
    }

    @Bean
    public ICategoryServicePort categoryServicePort() {
        return new CategoryUseCase(categoryPersistencePort());
    }

    @Bean
    public IOrderPersistencePort orderPersistencePort() {
        return new OrderJpaAdapter(orderRepository, orderEntityMapper);
    }

    @Bean
    public IOrderServicePort orderServicePort() {
        return new OrderUseCase(orderPersistencePort(), orderDishPersistencePort());
    }

    @Bean
    public IOrderDishPersistencePort orderDishPersistencePort(){
        return new OrderDishJpaAdapter(orderDishRepository, orderDishEntityMapper);
    }


}
