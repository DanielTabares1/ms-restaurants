package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.application.dto.RoleResponse;
import com.daniel.ms_restaurants.domain.model.UserResponse;
import com.daniel.ms_restaurants.domain.api.IJwtServicePort;
import com.daniel.ms_restaurants.domain.exception.DishNotFoundException;
import com.daniel.ms_restaurants.domain.exception.ErrorMessages;
import com.daniel.ms_restaurants.domain.exception.UserNotOwnerOfRestaurantException;
import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.spi.IDishPersistencePort;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtTokenHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.daniel.ms_restaurants.TestConstants.*;

class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IJwtServicePort jwtService;

    @Mock
    private UserFeignClient userFeignClient;

    @InjectMocks
    private DishUseCase dishUseCase;

    private Dish testDish;
    private Restaurant testRestaurant;
    private UserResponse testUserResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testRestaurant = new Restaurant(RESTAURANT_ID, RESTAURANT_NAME, "123 Street", OWNER_ID, "+1234567890", "logoUrl", "123456789");

        testDish = new Dish(DISH_ID, DISH_NAME, CATEGORY, DISH_DESCRIPTION, DISH_PRICE, testRestaurant, "imageUrl", true);

        RoleResponse ownerRole = new RoleResponse(1L, OWNER_ROLE_NAME, "Owner role description");
        testUserResponse = new UserResponse(OWNER_ID, USER_NAME, USER_LAST_NAME, USER_DOCUMENT_NUMBER, USER_PHONE, null, USER_EMAIL, USER_PASSWORD, ownerRole);

        JwtTokenHolder.setToken("mockedToken");
    }

    @Test
    void testCreateDish_WhenUserIsNotOwner_ShouldThrowException() {
        testRestaurant.setOwnerId(NON_EXISTENT_OWNER_ID);
        testDish.setRestaurant(testRestaurant);

        when(jwtService.extractUsername(anyString())).thenReturn(USER_EMAIL);
        when(userFeignClient.findByEmail(anyString())).thenReturn(testUserResponse);

        UserNotOwnerOfRestaurantException exception = assertThrows(UserNotOwnerOfRestaurantException.class, () -> {
            dishUseCase.createDish(testDish);
        });

        assertEquals(ErrorMessages.USER_NOT_OWNER_OF_RESTAURANT_WITH_DISH.getMessage(testDish.getId()), exception.getMessage());
    }

    @Test
    void testCreateDish_WhenUserIsOwner_ShouldCallPersistencePort() {
        when(jwtService.extractUsername(anyString())).thenReturn(USER_EMAIL);
        when(userFeignClient.findByEmail(anyString())).thenReturn(testUserResponse);
        when(dishPersistencePort.createDish(any(Dish.class))).thenReturn(testDish);

        Dish result = dishUseCase.createDish(testDish);

        assertEquals(testDish, result);
        verify(dishPersistencePort, times(1)).createDish(testDish);
    }

    @Test
    void testGetDishById_WhenDishExists_ShouldReturnDish() {
        when(dishPersistencePort.getDishById(DISH_ID)).thenReturn(Optional.of(testDish));

        Dish result = dishUseCase.getDishById(DISH_ID);

        assertEquals(testDish, result);
    }

    @Test
    void testGetDishById_WhenDishDoesNotExist_ShouldThrowException() {
        when(dishPersistencePort.getDishById(DISH_ID)).thenReturn(Optional.empty());

        DishNotFoundException exception = assertThrows(DishNotFoundException.class, () -> {
            dishUseCase.getDishById(DISH_ID);
        });

        assertEquals(ErrorMessages.DISH_NOT_FOUND.getMessage(DISH_ID), exception.getMessage());
    }

    @Test
    void testEditDish_WhenUserIsOwner_ShouldCallEditInPersistencePort() {
        when(jwtService.extractUsername(anyString())).thenReturn(USER_EMAIL);
        when(userFeignClient.findByEmail(anyString())).thenReturn(testUserResponse);
        when(dishPersistencePort.editDish(DISH_ID, testDish)).thenReturn(testDish);

        Dish result = dishUseCase.editDish(DISH_ID, testDish);

        assertEquals(testDish, result);
        verify(dishPersistencePort, times(1)).editDish(DISH_ID, testDish);
    }

    @Test
    void testEditDish_WhenUserIsNotOwner_ShouldThrowException() {

        testRestaurant.setOwnerId(NON_EXISTENT_OWNER_ID);
        testDish.setRestaurant(testRestaurant);

        when(jwtService.extractUsername(anyString())).thenReturn(USER_EMAIL);
        when(userFeignClient.findByEmail(anyString())).thenReturn(testUserResponse);

        UserNotOwnerOfRestaurantException exception = assertThrows(UserNotOwnerOfRestaurantException.class, () -> {
            dishUseCase.editDish(DISH_ID, testDish);
        });

        assertEquals(ErrorMessages.USER_NOT_OWNER_OF_RESTAURANT_WITH_DISH.getMessage(DISH_ID), exception.getMessage());
    }

    @Test
    void testFindAllDishesByRestaurantId_ShouldReturnDishes() {
        List<Dish> dishes = List.of(testDish);
        when(dishPersistencePort.getAllDishesByRestaurantId(RESTAURANT_ID, PAGE_NUMBER, PAGE_SIZE)).thenReturn(dishes);

        List<Dish> result = dishUseCase.findAllDishesByRestaurantId(RESTAURANT_ID, PAGE_NUMBER, PAGE_SIZE);

        assertEquals(dishes, result);
        verify(dishPersistencePort, times(1)).getAllDishesByRestaurantId(RESTAURANT_ID, PAGE_NUMBER, PAGE_SIZE);
    }

    @Test
    void testFindAllDishesByRestaurantIdByCategoryId_ShouldReturnDishes() {
        List<Dish> dishes = List.of(testDish);
        when(dishPersistencePort.getAllDishesByRestaurantIdByCategoryId(RESTAURANT_ID, CATEGORY_ID, PAGE_NUMBER, PAGE_SIZE)).thenReturn(dishes);

        List<Dish> result = dishUseCase.findAllDishesByRestaurantIdByCategoryId(RESTAURANT_ID, CATEGORY_ID, PAGE_NUMBER, PAGE_SIZE);

        assertEquals(dishes, result);
        verify(dishPersistencePort, times(1)).getAllDishesByRestaurantIdByCategoryId(RESTAURANT_ID, CATEGORY_ID, PAGE_NUMBER, PAGE_SIZE);
    }
}
