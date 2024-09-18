package com.daniel.ms_restaurants.application.handler;

import com.daniel.ms_restaurants.application.dto.CreateDishRequest;
import com.daniel.ms_restaurants.application.dto.EditDishRequest;
import com.daniel.ms_restaurants.application.mapper.IDishRequestMapper;
import com.daniel.ms_restaurants.domain.api.ICategoryServicePort;
import com.daniel.ms_restaurants.domain.api.IDishServicePort;
import com.daniel.ms_restaurants.domain.api.IRestaurantServicePort;
import com.daniel.ms_restaurants.domain.model.Category;
import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class DishHandler implements IDishHandler{

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IRestaurantServicePort restaurantServicePort;
    private final ICategoryServicePort categoryServicePort;

    @Override
    public Dish saveDish(CreateDishRequest dishRequest) {
        Dish dish = dishRequestMapper.toModel(dishRequest);
        Category category = categoryServicePort.getCategoryById(dishRequest.getIdCategory());
        dish.setCategory(category);
        //todo - Found restaurant based on Authentication with database
        Restaurant restaurant = restaurantServicePort.getRestaurantById(1);
        dish.setRestaurant(restaurant);
        return dishServicePort.createDish(dish);
    }

    @Override
    public Dish editDish(long dishId, EditDishRequest dishRequest) {
        Dish originalDish = dishServicePort.getDishById(dishId);
        originalDish.setPrice(dishRequest.getPrice());
        originalDish.setDescription(dishRequest.getDescription());
        return dishServicePort.editDish(dishId, originalDish);
    }
}