package com.daniel.ms_restaurants.application.mapper.impl;

import com.daniel.ms_restaurants.application.dto.DishResponse;
import com.daniel.ms_restaurants.application.mapper.IDishResponseMapper;
import com.daniel.ms_restaurants.domain.model.Dish;
import org.springframework.stereotype.Component;

@Component
public class DishResponseMapper implements IDishResponseMapper {
    @Override
    public DishResponse toDishResponse(Dish dish) {
        return new DishResponse(
                dish.getId(),
                dish.getName(),
                dish.getCategory().getName(),
                dish.getDescription(),
                dish.getPrice(),
                dish.getImageUrl(),
                dish.isActive()
        );
    }
}
