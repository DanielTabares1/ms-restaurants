package com.daniel.ms_restaurants.application.handler;

import com.daniel.ms_restaurants.domain.exception.OwnerNotFoundException;
import com.daniel.ms_restaurants.application.handler.impl.RestaurantHandler;
import com.daniel.ms_restaurants.domain.api.IRestaurantServicePort;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.application.dto.RoleResponse;
import com.daniel.ms_restaurants.application.dto.UserResponse;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantHandlerTest {

    @InjectMocks
    private RestaurantHandler restaurantHandler;

    @Mock
    private IRestaurantServicePort IRestaurantServicePort;

    @Mock
    private UserFeignClient userFeignClient;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveRestaurant() {
        Restaurant restaurant = new Restaurant(
                1L,                // id
                "The Food Place",   // name
                "123 Main St",      // address
                100L,               // ownerId
                "5551234",          // phoneNumber
                "http://logo.com/logo.png", // logoUrl
                "123456789"         // nit
        );

        RoleResponse ownerRole = new RoleResponse(4L, "OWNER", "EL dueño de un chuzo");
        UserResponse owner = new UserResponse(100L, "John", "Doe", "12345678", "5555555", new Date(), "owner@example.com", "password", ownerRole);

        // Simula que el propietario existe y tiene el rol OWNER
        when(userFeignClient.getUserById(restaurant.getOwnerId())).thenReturn(owner);

        // Simula el comportamiento del servicio
        when(IRestaurantServicePort.saveRestaurant(any(Restaurant.class))).thenReturn(restaurant);

        // Act: Llama al método bajo prueba
        Restaurant result = restaurantHandler.saveRestaurant(restaurant);

        // Assert: Verifica que el resultado no sea nulo y que los datos sean correctos
        assertNotNull(result);
        assertEquals(restaurant.getId(), result.getId());
        assertEquals(restaurant.getName(), result.getName());
        assertEquals(restaurant.getAddress(), result.getAddress());
        assertEquals(restaurant.getOwnerId(), result.getOwnerId());
        assertEquals(restaurant.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(restaurant.getLogoUrl(), result.getLogoUrl());
        assertEquals(restaurant.getNit(), result.getNit());

        // Verifica que los métodos de los mocks fueron llamados correctamente
        verify(userFeignClient, times(1)).getUserById(restaurant.getOwnerId());
        verify(IRestaurantServicePort, times(1)).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void saveRestaurant_FailsWhenOwnerNotFound() {
        // Arrange: Prepara los datos de prueba
        Restaurant restaurant = new Restaurant(
                1L,                // id
                "The Food Place",   // name
                "123 Main St",      // address
                100L,               // ownerId
                "5551234",          // phoneNumber
                "http://logo.com/logo.png", // logoUrl
                "123456789"         // nit
        );

        // Simula que el propietario no existe
        when(userFeignClient.getUserById(restaurant.getOwnerId())).thenReturn(null);

        // Act & Assert: Verifica que se lance la excepción OwnerNotFoundException
        assertThrows(OwnerNotFoundException.class, () -> {
            restaurantHandler.saveRestaurant(restaurant);
        });

        // Verifica que el método de FeignClient fue llamado
        verify(userFeignClient, times(1)).getUserById(restaurant.getOwnerId());
        // Verifica que el servicio no fue llamado
        verify(IRestaurantServicePort, times(0)).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void saveRestaurant_FailsWhenUserIsNotOwner() {
        // Arrange: Prepara los datos de prueba
        Restaurant restaurant = new Restaurant(
                1L,                // id
                "The Food Place",   // name
                "123 Main St",      // address
                100L,               // ownerId
                "5551234",          // phoneNumber
                "http://logo.com/logo.png", // logoUrl
                "123456789"         // nit
        );

        RoleResponse nonOwnerRole = new RoleResponse(2L, "CLIENT", "Client role");
        UserResponse nonOwner = new UserResponse(100L, "John", "Doe", "12345678", "5555555", new Date(), "client@example.com", "password", nonOwnerRole);

        // Simula que el usuario existe pero no tiene el rol de OWNER
        when(userFeignClient.getUserById(restaurant.getOwnerId())).thenReturn(nonOwner);

        // Act & Assert: Verifica que se lance la excepción OwnerNotFoundException
        assertThrows(OwnerNotFoundException.class, () -> {
            restaurantHandler.saveRestaurant(restaurant);
        });

        // Verifica que el método de FeignClient fue llamado
        verify(userFeignClient, times(1)).getUserById(restaurant.getOwnerId());
        // Verifica que el servicio no fue llamado
        verify(IRestaurantServicePort, times(0)).saveRestaurant(any(Restaurant.class));
    }


}