package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.TestConstants;
import com.daniel.ms_restaurants.application.dto.RoleResponse;
import com.daniel.ms_restaurants.application.dto.UserResponse;
import com.daniel.ms_restaurants.domain.exception.ErrorMessages;
import com.daniel.ms_restaurants.domain.exception.OwnerNotFoundException;
import com.daniel.ms_restaurants.domain.exception.RestaurantNotFoundException;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.spi.IRestaurantPersistencePort;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.daniel.ms_restaurants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantUseCaseTest {

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    @Mock
    private IRestaurantPersistencePort IRestaurantPersistencePort;

    @Mock
    private UserFeignClient userFeignClient;

    private Restaurant restaurant;
    private RoleResponse ownerRole;
    private RoleResponse clientRole;
    private UserResponse ownerUser;
    private UserResponse clientUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Define valores constantes para usar en los tests
        restaurant = new Restaurant();
        restaurant.setOwnerId(TestConstants.OWNER_ID);
        restaurant.setId(RESTAURANT_ID);

        ownerRole = new RoleResponse(OWNER_ID, OWNER_ROLE_VALUE, "Restaurant owner");
        clientRole = new RoleResponse(OWNER_ID, NON_OWNER_ROLE_NAME, "Client role");

        ownerUser = new UserResponse(OWNER_ID, "John", "Doe", "123456", "1234567890", null, USER_EMAIL, "password", ownerRole);
        clientUser = new UserResponse(OWNER_ID, "John", "Doe", "123456", "1234567890", null, USER_EMAIL, "password", clientRole);

    }

    @Test
    void saveRestaurant_ShouldSaveRestaurant_WhenOwnerExists() {
        // Arrange
        when(userFeignClient.getUserById(OWNER_ID)).thenReturn(ownerUser);
        when(IRestaurantPersistencePort.saveRestaurant(any(Restaurant.class))).thenReturn(restaurant);

        // Act
        Restaurant result = restaurantUseCase.saveRestaurant(restaurant);

        // Assert
        assertNotNull(result);
        verify(IRestaurantPersistencePort).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void saveRestaurant_ShouldThrowOwnerNotFoundException_WhenOwnerDoesNotExist() {
        // Arrange
        when(userFeignClient.getUserById(OWNER_ID)).thenReturn(null);

        // Act & Assert
        OwnerNotFoundException exception = assertThrows(OwnerNotFoundException.class,
                () -> restaurantUseCase.saveRestaurant(restaurant));

        assertEquals(ErrorMessages.OWNER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void saveRestaurant_ShouldThrowOwnerNotFoundException_WhenUserIsNotOwner() {
        // Arrange
        when(userFeignClient.getUserById(OWNER_ID)).thenReturn(clientUser);

        // Act & Assert
        OwnerNotFoundException exception = assertThrows(OwnerNotFoundException.class,
                () -> restaurantUseCase.saveRestaurant(restaurant));

        assertEquals(ErrorMessages.OWNER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void houldGetRestaurantById_WhenExists() {
        // Arrange
        when(IRestaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(restaurant));

        // Act
        Restaurant result = restaurantUseCase.getRestaurantById(RESTAURANT_ID);
        // Assert
        assertNotNull(result);
        assertEquals(restaurant, result);
        verify(IRestaurantPersistencePort).getRestaurantById(RESTAURANT_ID);
    }

    @Test
    void getRestaurantById_ShouldThrowRestaurantNotFoundException_WhenRestaurantDoesNotExist() {
        // Arrange
        when(IRestaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class,
                () -> restaurantUseCase.getRestaurantById(RESTAURANT_ID));

        assertEquals(ErrorMessages.RESTAURANT_NOT_FOUND.getMessage(RESTAURANT_ID), exception.getMessage());
    }

}