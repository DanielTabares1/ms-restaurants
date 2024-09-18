package com.daniel.ms_restaurants.application.handler;

import com.daniel.ms_restaurants.application.dto.CreateDishRequest;
import com.daniel.ms_restaurants.application.mapper.IDishRequestMapper;
import com.daniel.ms_restaurants.domain.api.ICategoryServicePort;
import com.daniel.ms_restaurants.domain.api.IDishServicePort;
import com.daniel.ms_restaurants.domain.api.IRestaurantServicePort;
import com.daniel.ms_restaurants.domain.model.Category;
import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.infrastructure.exception.CategoryNotFoundException;
import com.daniel.ms_restaurants.infrastructure.exception.RestaurantNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DishHandlerTest {

    @InjectMocks
    private DishHandler dishHandler;

    @Mock
    private IDishServicePort dishServicePort;

    @Mock
    private IDishRequestMapper dishRequestMapper;

    @Mock
    private IRestaurantServicePort restaurantServicePort;

    @Mock
    private ICategoryServicePort categoryServicePort;


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void saveDish() {
        // Given
        CreateDishRequest request = new CreateDishRequest();
        request.setIdCategory(1L);

        Dish dish = new Dish();
        Category category = new Category();
        Restaurant restaurant = new Restaurant();

        // Mock the behavior of mapper and services
        when(dishRequestMapper.toModel(request)).thenReturn(dish);
        when(categoryServicePort.getCategoryById(request.getIdCategory())).thenReturn(category);
        when(restaurantServicePort.getRestaurantById(1L)).thenReturn(restaurant);
        when(dishServicePort.createDish(dish)).thenReturn(dish);

        // When
        Dish savedDish = dishHandler.saveDish(request);

        // Then
        assertNotNull(savedDish);
        verify(dishRequestMapper, times(1)).toModel(request);
        verify(categoryServicePort, times(1)).getCategoryById(request.getIdCategory());
        verify(restaurantServicePort, times(1)).getRestaurantById(1L);
        verify(dishServicePort, times(1)).createDish(dish);
        assertEquals(category, savedDish.getCategory());
        assertEquals(restaurant, savedDish.getRestaurant());
    }

    @Test
    void saveDishReturnsCategoryNotFound() {
        CreateDishRequest request = new CreateDishRequest();
        request.setIdCategory(1L);

        Dish dish = new Dish();


        when(dishRequestMapper.toModel(request)).thenReturn(dish);
        when(categoryServicePort.getCategoryById(request.getIdCategory())).thenThrow(new CategoryNotFoundException("Category not found"));

        assertThrows(CategoryNotFoundException.class, () -> dishHandler.saveDish(request));
        verify(categoryServicePort, times(1)).getCategoryById(request.getIdCategory());
        verify(dishServicePort, times(0)).createDish(any(Dish.class));  // Ensure this is never called
    }

    @Test
    void saveDishWhenRestaurantNotFound() {
        CreateDishRequest request = new CreateDishRequest();
        request.setIdCategory(1L);

        Dish dish = new Dish();
        Category category = new Category();

        when(dishRequestMapper.toModel(request)).thenReturn(dish);
        when(categoryServicePort.getCategoryById(request.getIdCategory())).thenReturn(category);
        when(restaurantServicePort.getRestaurantById(1L)).thenThrow(new RestaurantNotFoundException("Restaurant not found"));

        assertThrows(RestaurantNotFoundException.class, () -> dishHandler.saveDish(request));
        verify(restaurantServicePort, times(1)).getRestaurantById(1L);
        verify(dishServicePort, times(0)).createDish(any(Dish.class));  // Ensure this is never called
    }

}