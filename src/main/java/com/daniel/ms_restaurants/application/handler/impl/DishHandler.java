package com.daniel.ms_restaurants.application.handler.impl;

import com.daniel.ms_restaurants.application.dto.CreateDishRequest;
import com.daniel.ms_restaurants.application.dto.EditDishRequest;
import com.daniel.ms_restaurants.application.dto.ToggleActivationToDishRequest;
import com.daniel.ms_restaurants.application.handler.IDishHandler;
import com.daniel.ms_restaurants.application.mapper.IDishRequestMapper;
import com.daniel.ms_restaurants.domain.api.ICategoryServicePort;
import com.daniel.ms_restaurants.domain.api.IDishServicePort;
import com.daniel.ms_restaurants.domain.api.IRestaurantServicePort;
import com.daniel.ms_restaurants.domain.model.Category;
import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.model.UserResponse;
import com.daniel.ms_restaurants.infrastructure.exception.UserNotOwnerOfRestaurantException;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtService;
import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtTokenHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IRestaurantServicePort restaurantServicePort;
    private final ICategoryServicePort categoryServicePort;
    private final JwtService jwtService;
    private final UserFeignClient userFeignClient;

    @Override
    public Dish saveDish(CreateDishRequest dishRequest) {
        Restaurant restaurant = restaurantServicePort.getRestaurantById(dishRequest.getIdRestaurant());

        if(!userIsOwnerOfRestaurant(restaurant)){
            throw new UserNotOwnerOfRestaurantException("Authenticated user is not the owner of the restaurant with id: " + dishRequest.getIdRestaurant());
        }

        Dish dish = dishRequestMapper.toModel(dishRequest);
        Category category = categoryServicePort.getCategoryById(dishRequest.getIdCategory());
        dish.setCategory(category);

        dish.setRestaurant(restaurant);
        return dishServicePort.createDish(dish);
    }

    @Override
    public Dish editDish(long dishId, EditDishRequest dishRequest) {
        Dish originalDish = dishServicePort.getDishById(dishId);

        if (!userIsOwnerOfRestaurant(originalDish.getRestaurant())) {
            throw new UserNotOwnerOfRestaurantException("Authenticated user is not the owner of the restaurant which offers dish with id: " + dishId);
        }

        originalDish.setPrice(dishRequest.getPrice());
        originalDish.setDescription(dishRequest.getDescription());
        return dishServicePort.editDish(dishId, originalDish);
    }

    @Override
    public Dish toggleActivation(long dishId, ToggleActivationToDishRequest req) {
        Dish originalDish = dishServicePort.getDishById(dishId);

        if (!userIsOwnerOfRestaurant(originalDish.getRestaurant())) {
            throw new UserNotOwnerOfRestaurantException("Authenticated user is not the owner of the restaurant which offers dish with id: " + dishId);
        }

        originalDish.setActive(req.isActivate());
        return dishServicePort.editDish(dishId, originalDish);
    }


    public boolean userIsOwnerOfRestaurant(Restaurant restaurant) {
        String email = jwtService.extractUsername(JwtTokenHolder.getToken());
        UserResponse userResponse = userFeignClient.findByEmail(email);
        return restaurant.getOwnerId() == userResponse.getId();
    }
}
