package com.daniel.ms_restaurants.infrastructure.config;

import com.daniel.ms_restaurants.domain.api.*;
import com.daniel.ms_restaurants.domain.spi.*;
import com.daniel.ms_restaurants.domain.usecase.*;
import com.daniel.ms_restaurants.infrastructure.feignclient.SmsFeignClient;
import com.daniel.ms_restaurants.infrastructure.feignclient.TraceabilityFeignClient;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import com.daniel.ms_restaurants.infrastructure.feignclient.adapter.SmsClientAdapter;
import com.daniel.ms_restaurants.infrastructure.feignclient.adapter.TraceabilityClientAdapter;
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

    private final IEmployeeRestaurantEntityMapper employeeRestaurantEntityMapper;
    private final IEmployeeRestaurantRepository employeeRestaurantRepository;

    private final IJwtServicePort jwtServicePort;
    private final UserFeignClient userFeignClient;
    private final SmsFeignClient smsFeignClient;
    private final TraceabilityFeignClient traceabilityFeignClient;


    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort() {
        return new IRestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort() {
        return new RestaurantUseCase(restaurantPersistencePort(), userFeignClient);
    }


    @Bean
    public IDishPersistencePort dishPersistencePort() {
        return new DishJpaAdapter(dishRepository, dishEntityMapper);
    }

    @Bean
    public IDishServicePort dishServicePort() {
        return new DishUseCase(dishPersistencePort(), jwtServicePort, userFeignClient);
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
        return new OrderUseCase(orderPersistencePort(), orderDishPersistencePort(), userFeignClient, smsPersistencePort(), jwtServicePort, employeeRestaurantPersistencePort(), traceabilityPersistencePort());
    }

    @Bean
    public IOrderDishPersistencePort orderDishPersistencePort() {
        return new OrderDishJpaAdapter(orderDishRepository, orderDishEntityMapper);
    }

    @Bean
    public IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort() {
        return new EmployeeRestaurantJpaAdapter(employeeRestaurantRepository, employeeRestaurantEntityMapper);
    }

    @Bean
    public IEmployeeRestaurantServicePort employeeRestaurantServicePort() {
        return new EmployeeRestaurantUseCase(employeeRestaurantPersistencePort(), restaurantPersistencePort(), jwtServicePort, userFeignClient);
    }

    @Bean
    public ISmsPersistencePort smsPersistencePort() {
        return new SmsClientAdapter(smsFeignClient);
    }

    @Bean
    public ITraceabilityPersistencePort traceabilityPersistencePort() {
        return new TraceabilityClientAdapter(traceabilityFeignClient);
    }

    @Bean
    public ITraceabilityServicePort traceabilityServicePort() {
        return new TraceabilityUseCase(traceabilityPersistencePort(), jwtServicePort, userFeignClient, orderPersistencePort());
    }


}
