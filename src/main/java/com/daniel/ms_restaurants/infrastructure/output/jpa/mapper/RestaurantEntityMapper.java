package com.daniel.ms_restaurants.infrastructure.output.jpa.mapper;

import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.RestaurantEntity;
import org.springframework.stereotype.Component;

@Component
public class RestaurantEntityMapper implements IRestaurantEntityMapper{
    @Override
    public RestaurantEntity toEntity(Restaurant restaurant) {

        if (restaurant == null) {
            return null;
        }

        return new RestaurantEntity(
                    restaurant.getId(),
                    restaurant.getName(),
                    restaurant.getAddress(),
                    restaurant.getOwnerId(),
                    restaurant.getPhoneNumber(),
                    restaurant.getLogoUrl(),
                    restaurant.getNit()
        );
    }

    @Override
    public Restaurant toModel(RestaurantEntity restaurantEntity) {
        if (restaurantEntity == null){
            return null;
        }
        return new Restaurant(
                restaurantEntity.getId(),
                restaurantEntity.getName(),
                restaurantEntity.getAddress(),
                restaurantEntity.getOwnerId(),
                restaurantEntity.getPhoneNumber(),
                restaurantEntity.getLogoUrl(),
                restaurantEntity.getNit()
        );
    }
}
