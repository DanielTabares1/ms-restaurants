package com.daniel.ms_restaurants.application.mapper;

import com.daniel.ms_restaurants.application.dto.CreateDishRequest;
import com.daniel.ms_restaurants.domain.model.Dish;
import org.springframework.stereotype.Component;

@Component
public class DishRequestMapper implements IDishRequestMapper{
    @Override
    public Dish toModel(CreateDishRequest dishRequest) {
        if (dishRequest == null){
            return null;
        }

        return new Dish(
                null,
                dishRequest.getName(),
                null,
                dishRequest.getDescription(),
                dishRequest.getPrice(),
                null,
                dishRequest.getImageUrl(),
                true
        );
    }
}
