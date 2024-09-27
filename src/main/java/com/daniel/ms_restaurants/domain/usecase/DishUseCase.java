package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.application.dto.UserResponse;
import com.daniel.ms_restaurants.domain.api.IDishServicePort;
import com.daniel.ms_restaurants.domain.api.IJwtServicePort;
import com.daniel.ms_restaurants.domain.exception.DishNotFoundException;
import com.daniel.ms_restaurants.domain.exception.ErrorMessages;
import com.daniel.ms_restaurants.domain.exception.UserNotOwnerOfRestaurantException;
import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.spi.IDishPersistencePort;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtTokenHolder;

import java.util.List;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IJwtServicePort jwtService;
    private final UserFeignClient userFeignClient;

    public DishUseCase(IDishPersistencePort dishPersistencePort, IJwtServicePort jwtService, UserFeignClient userFeignClient) {
        this.dishPersistencePort = dishPersistencePort;
        this.jwtService = jwtService;
        this.userFeignClient = userFeignClient;
    }

    @Override
    public Dish createDish(Dish dish) {
        if (!userIsOwnerOfRestaurant(dish.getRestaurant())) {
            throw new UserNotOwnerOfRestaurantException(ErrorMessages.USER_NOT_OWNER_OF_RESTAURANT_WITH_DISH.getMessage(dish.getId()));
        }
        return dishPersistencePort.createDish(dish);
    }

    @Override
    public Dish getDishById(long id) {
        return dishPersistencePort.getDishById(id).orElseThrow(
                () -> new DishNotFoundException(ErrorMessages.DISH_NOT_FOUND.getMessage(id))
        );
    }

    @Override
    public List<Dish> findAllDishesByRestaurantId(long restaurantId, int pageNumber, int pageSize) {
        return dishPersistencePort.getAllDishesByRestaurantId(restaurantId, pageNumber, pageSize);
    }

    @Override
    public List<Dish> findAllDishesByRestaurantIdByCategoryId(long restaurantId, long categoryId, int pageNumber, int pageSize) {
        return dishPersistencePort.getAllDishesByRestaurantIdByCategoryId(restaurantId, categoryId, pageNumber, pageSize);
    }

    @Override
    public Dish editDish(long dishId, Dish editedDish) {
        if (!userIsOwnerOfRestaurant(editedDish.getRestaurant())) {
            throw new UserNotOwnerOfRestaurantException(ErrorMessages.USER_NOT_OWNER_OF_RESTAURANT_WITH_DISH.getMessage(dishId));
        }
        return dishPersistencePort.editDish(dishId, editedDish);
    }

    public boolean userIsOwnerOfRestaurant(Restaurant restaurant) {
        String email = jwtService.extractUsername(JwtTokenHolder.getToken());
        UserResponse userResponse = userFeignClient.findByEmail(email);
        return restaurant.getOwnerId() == userResponse.getId();
    }


}
