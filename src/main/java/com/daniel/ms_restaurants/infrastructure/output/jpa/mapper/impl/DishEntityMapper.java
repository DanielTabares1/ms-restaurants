package com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.impl;

import com.daniel.ms_restaurants.domain.model.Category;
import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.CategoryEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.DishEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.RestaurantEntity;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.IDishEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class DishEntityMapper implements IDishEntityMapper {
    @Override
    public Dish toModel(DishEntity dishEntity) {

        Restaurant restaurant = getRestaurant(dishEntity);
        Category category = getCategory(dishEntity);
        return new Dish(
                dishEntity.getId(),
                dishEntity.getName(),
                category,
                dishEntity.getDescription(),
                dishEntity.getPrice(),
                restaurant,
                dishEntity.getImageUrl(),
                dishEntity.isActive()
        );
    }

    @Override
    public DishEntity toEntity(Dish dish) {
        RestaurantEntity restaurant = getRestaurantEntity(dish);
        CategoryEntity category = getCategoryEntity(dish);
        return new DishEntity(
                dish.getId(),
                dish.getName(),
                category,
                dish.getDescription(),
                dish.getPrice(),
                restaurant,
                dish.getImageUrl(),
                dish.isActive()
        );
    }

    private static CategoryEntity getCategoryEntity(Dish dish) {
        CategoryEntity category;
        if (dish.getCategory() == null){
            category = null;
        }
        else {
            category = new CategoryEntity(
                    dish.getCategory().getId(),
                    dish.getCategory().getName(),
                    dish.getCategory().getDescription()
            );
        }
        return category;
    }

    private static RestaurantEntity getRestaurantEntity(Dish dish) {
        RestaurantEntity restaurant;
        if(dish.getRestaurant() == null){
            restaurant = null;
        }
        else {
            restaurant = new RestaurantEntity(
                    dish.getRestaurant().getId(),
                    dish.getRestaurant().getName(),
                    dish.getRestaurant().getAddress(),
                    dish.getRestaurant().getOwnerId(),
                    dish.getRestaurant().getPhoneNumber(),
                    dish.getRestaurant().getLogoUrl(),
                    dish.getRestaurant().getNit()
            );
        }
        return restaurant;
    }

    private static Category getCategory(DishEntity dishEntity) {
        Category category;
        if  (dishEntity.getRestaurant()==null){
            category = null;
        }
        else {
            category = new Category(
                    dishEntity.getCategory().getId(),
                    dishEntity.getCategory().getName(),
                    dishEntity.getCategory().getDescription()
            );
        }
        return category;
    }

    private static Restaurant getRestaurant(DishEntity dishEntity) {
        Restaurant restaurant;
        if (dishEntity.getRestaurant()==null){
            restaurant = null;
        }
        else {
            restaurant = new Restaurant(
                    dishEntity.getRestaurant().getId(),
                    dishEntity.getRestaurant().getName(),
                    dishEntity.getRestaurant().getAddress(),
                    dishEntity.getRestaurant().getOwnerId(),
                    dishEntity.getRestaurant().getPhoneNumber(),
                    dishEntity.getRestaurant().getLogoUrl(),
                    dishEntity.getRestaurant().getNit()
            );
        }
        return restaurant;
    }
}
