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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IRestaurantServicePort restaurantServicePort;
    private final ICategoryServicePort categoryServicePort;

    @Override
    public Dish saveDish(CreateDishRequest dishRequest) {
        Dish dish = dishRequestMapper.toModel(dishRequest);
        Restaurant restaurant = restaurantServicePort.getRestaurantById(dishRequest.getIdRestaurant());
        Category category = categoryServicePort.getCategoryById(dishRequest.getIdCategory());
        dish.setCategory(category);
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

    @Override
    public Dish toggleActivation(long dishId, ToggleActivationToDishRequest req) {
        Dish originalDish = dishServicePort.getDishById(dishId);
        originalDish.setActive(req.isActivate());
        return dishServicePort.editDish(dishId, originalDish);
    }


    @Override
    public List<Dish> getAllDishesByRestaurantId(long restaurantId, int pageNumber, int pageSize) {
        return dishServicePort.findAllDishesByRestaurantId(restaurantId, pageNumber, pageSize);
    }

    @Override
    public List<Dish> getAllDishesByRestaurantIdByCategoryId(long restaurantId, long categoryId, int pageNumber, int pageSize) {
        return dishServicePort.findAllDishesByRestaurantIdByCategoryId(restaurantId, categoryId, pageNumber, pageSize);
    }
}
