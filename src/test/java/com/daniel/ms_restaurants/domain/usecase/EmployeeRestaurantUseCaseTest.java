package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.application.dto.RoleResponse;
import com.daniel.ms_restaurants.domain.model.UserResponse;
import com.daniel.ms_restaurants.domain.api.IJwtServicePort;
import com.daniel.ms_restaurants.domain.exception.ErrorMessages;
import com.daniel.ms_restaurants.domain.exception.RestaurantNotFoundException;
import com.daniel.ms_restaurants.domain.exception.UserNotAnEmployeeException;
import com.daniel.ms_restaurants.domain.exception.UserNotOwnerOfRestaurantException;
import com.daniel.ms_restaurants.domain.model.EmployeeRestaurant;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.model.enums.UserRoles;
import com.daniel.ms_restaurants.domain.spi.IEmployeeRestaurantPersistencePort;
import com.daniel.ms_restaurants.domain.spi.IRestaurantPersistencePort;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtTokenHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.daniel.ms_restaurants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeRestaurantUseCaseTest {

    @Mock
    private IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IJwtServicePort jwtService;

    @Mock
    private UserFeignClient userFeignClient;

    @InjectMocks
    private EmployeeRestaurantUseCase employeeRestaurantUseCase;

    private EmployeeRestaurant testEmployeeRestaurant;
    private Restaurant testRestaurant;
    private UserResponse testUserResponse;
    private UserResponse testOwner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testRestaurant = new Restaurant(RESTAURANT_ID, RESTAURANT_NAME, "123 Street", OWNER_ID, "+1234567890", "logoUrl", "123456789");

        testEmployeeRestaurant = new EmployeeRestaurant(EMPLOYEE_ID,  RESTAURANT_ID, EMPLOYEE_EMAIL);

        RoleResponse employeeRole = new RoleResponse(EMPLOYEE_ROLE_ID, EMPLOYEE_ROLE_NAME, "Employee role description");
        testUserResponse = new UserResponse(EMPLOYEE_ID, EMPLOYEE_NAME, EMPLOYEE_LAST_NAME, EMPLOYEE_DOCUMENT_NUMBER, EMPLOYEE_PHONE, null, EMPLOYEE_EMAIL, EMPLOYEE_PASSWORD, employeeRole);

        RoleResponse ownerRole = new RoleResponse(OWNER_ID, OWNER_ROLE_NAME, "Owner role description");
        testOwner = new UserResponse(OWNER_ID, EMPLOYEE_NAME, EMPLOYEE_LAST_NAME, EMPLOYEE_DOCUMENT_NUMBER, EMPLOYEE_PHONE, null, OWNER_EMAIL, EMPLOYEE_PASSWORD, ownerRole);

        JwtTokenHolder.setToken("mockedToken");
    }

    @Test
    void testSaveEmployee_WhenRestaurantNotFound_ShouldThrowException() {
        when(restaurantPersistencePort.getRestaurantById(anyLong())).thenReturn(Optional.empty());

        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class, () -> {
            employeeRestaurantUseCase.saveEmployee(testEmployeeRestaurant);
        });

        assertEquals(ErrorMessages.RESTAURANT_NOT_FOUND.getMessage(testEmployeeRestaurant.getRestaurantId()), exception.getMessage());
    }

    @Test
    void testSaveEmployee_WhenUserIsNotOwner_ShouldThrowException() {
        testRestaurant.setOwnerId(NON_EXISTENT_OWNER_ID);

        when(restaurantPersistencePort.getRestaurantById(anyLong())).thenReturn(Optional.of(testRestaurant));
        when(jwtService.extractUsername(anyString())).thenReturn(OWNER_EMAIL);
        when(userFeignClient.findByEmail(anyString())).thenReturn(testOwner);

        UserNotOwnerOfRestaurantException exception = assertThrows(UserNotOwnerOfRestaurantException.class, () -> {
            employeeRestaurantUseCase.saveEmployee(testEmployeeRestaurant);
        });

        assertEquals(ErrorMessages.USER_NOT_OWNER_OF_RESTAURANT.getMessage(testRestaurant.getId()), exception.getMessage());
    }

    @Test
    void testSaveEmployee_WhenUserIsOwner_ShouldCallPersistencePort() {
        when(restaurantPersistencePort.getRestaurantById(anyLong())).thenReturn(Optional.of(testRestaurant));
        when(jwtService.extractUsername(anyString())).thenReturn(OWNER_EMAIL);
        when(userFeignClient.findByEmail(anyString())).thenReturn(testOwner);
        when(employeeRestaurantPersistencePort.saveEmployee(any(EmployeeRestaurant.class))).thenReturn(testEmployeeRestaurant);

        EmployeeRestaurant result = employeeRestaurantUseCase.saveEmployee(testEmployeeRestaurant);

        assertEquals(testEmployeeRestaurant, result);
        verify(employeeRestaurantPersistencePort, times(1)).saveEmployee(testEmployeeRestaurant);
    }

    @Test
    void testUserIsOwnerOfRestaurant_WhenUserIsOwner_ShouldReturnTrue() {
        when(jwtService.extractUsername(anyString())).thenReturn(OWNER_EMAIL);
        when(userFeignClient.findByEmail(anyString())).thenReturn(testOwner);

        boolean isOwner = employeeRestaurantUseCase.userIsOwnerOfRestaurant(testRestaurant);

        assertTrue(isOwner);
    }

    @Test
    void testUserIsOwnerOfRestaurant_WhenUserIsNotOwner_ShouldReturnFalse() {
        testRestaurant.setOwnerId(NON_EXISTENT_OWNER_ID);

        when(jwtService.extractUsername(anyString())).thenReturn(OWNER_EMAIL);
        when(userFeignClient.findByEmail(anyString())).thenReturn(testOwner);

        boolean isOwner = employeeRestaurantUseCase.userIsOwnerOfRestaurant(testRestaurant);

        assertFalse(isOwner);
    }

    @Test
    void testGetRestaurantIdByEmail_WhenUserIsNotEmployee_ShouldThrowException() {
        testUserResponse.getRole().setName(UserRoles.CLIENT.toString());

        when(jwtService.extractUsername(anyString())).thenReturn(EMPLOYEE_EMAIL);
        when(userFeignClient.getUserByEmail(anyString())).thenReturn(testUserResponse);

        UserNotAnEmployeeException exception = assertThrows(UserNotAnEmployeeException.class, () -> {
            employeeRestaurantUseCase.getRestaurantIdByEmail(EMPLOYEE_EMAIL);
        });

        assertEquals(ErrorMessages.USER_NOT_AN_EMPLOYEE.getMessage(EMPLOYEE_EMAIL), exception.getMessage());
    }

    @Test
    void testGetRestaurantIdByEmail_WhenUserIsEmployee_ShouldReturnRestaurantId() {
        when(jwtService.extractUsername(anyString())).thenReturn(EMPLOYEE_EMAIL);
        when(userFeignClient.getUserByEmail(anyString())).thenReturn(testUserResponse);
        when(employeeRestaurantPersistencePort.getByEmployeeEmail(anyString())).thenReturn(testEmployeeRestaurant);

        long restaurantId = employeeRestaurantUseCase.getRestaurantIdByEmail(EMPLOYEE_EMAIL);

        assertEquals(RESTAURANT_ID, restaurantId);
        verify(employeeRestaurantPersistencePort, times(1)).getByEmployeeEmail(EMPLOYEE_EMAIL);
    }




}