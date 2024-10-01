package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.application.dto.RoleResponse;
import com.daniel.ms_restaurants.domain.model.UserResponse;
import com.daniel.ms_restaurants.domain.api.IJwtServicePort;
import com.daniel.ms_restaurants.domain.exception.*;
import com.daniel.ms_restaurants.domain.model.*;
import com.daniel.ms_restaurants.domain.model.enums.OrderStatus;
import com.daniel.ms_restaurants.domain.model.enums.UserRoles;
import com.daniel.ms_restaurants.domain.spi.*;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtTokenHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;
    @Mock
    private IOrderDishPersistencePort orderDishPersistencePort;
    @Mock
    private UserFeignClient userFeignClient;
    @Mock
    private ISmsPersistencePort smsPersistencePort;
    @Mock
    private IJwtServicePort jwtService;
    @Mock
    private IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;
    @Mock
    private ITraceabilityPersistencePort traceabilityPersistencePort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private static final String JWT_TOKEN = "sample-jwt-token";
    private static final long CLIENT_ID = 1L;
    private static final long RESTAURANT_ID = 2L;
    private static final long DISH_ID = 3L;
    private static final long ORDER_ID = 4L;
    private static final String CLIENT_EMAIL = "client@example.com";
    private static final String EMPLOYEE_EMAIL = "employee@example.com";

    private UserResponse clientResponse;
    private UserResponse employeeResponse;
    private Order order;
    private Order assignedOrder;
    private Dish dish;
    private Restaurant restaurant;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderUseCase = new OrderUseCase(orderPersistencePort, orderDishPersistencePort, userFeignClient, smsPersistencePort, jwtService, employeeRestaurantPersistencePort, traceabilityPersistencePort);

        // Setup JwtTokenHolder mock
        JwtTokenHolder.setToken(JWT_TOKEN);

        // Create reusable RoleResponse for client and employee
        RoleResponse clientRole = new RoleResponse(1L, UserRoles.CLIENT.toString(), "Client role");
        RoleResponse employeeRole = new RoleResponse(2L, UserRoles.EMPLOYEE.toString(), "Employee role");

        // Create reusable UserResponse for client and employee
        clientResponse = new UserResponse(CLIENT_ID, "Client Name", "Client Lastname", "123456789", "9876543210", new Date(), CLIENT_EMAIL, "password", clientRole);
        employeeResponse = new UserResponse(5L, "Employee Name", "Employee Lastname", "987654321", "0123456789", new Date(), EMPLOYEE_EMAIL, "password", employeeRole);

        // Create reusable category, restaurant, order, and dish
        category = new Category(1L, "Main Course", "Main category");
        restaurant = new Restaurant(RESTAURANT_ID, "Test Restaurant", "Address", CLIENT_ID, "123456789", "logo-url", "123-NIT");
        dish = new Dish(DISH_ID, "Test Dish", category, "Test description", 10, restaurant, "image-url", true);
        order = new Order(ORDER_ID, CLIENT_ID, new Date(), OrderStatus.PENDING.toString(), 0L, new HashSet<>(), restaurant);
        assignedOrder = new Order(ORDER_ID, CLIENT_ID, new Date(), OrderStatus.IN_PROGRESS.toString(), 5L, new HashSet<>(), restaurant);

    }

    @Test
    void createOrder_validClient_noActiveOrders() {
        when(jwtService.extractUsername(JWT_TOKEN)).thenReturn(CLIENT_EMAIL);
        when(userFeignClient.findByEmail(CLIENT_EMAIL)).thenReturn(clientResponse);
        when(orderPersistencePort.getByClientId(CLIENT_ID)).thenReturn(Collections.emptyList());
        when(orderPersistencePort.saveOrder(any(Order.class))).thenReturn(order);

        Order createdOrder = orderUseCase.createOrder(order);

        assertNotNull(createdOrder);
        assertEquals(CLIENT_ID, createdOrder.getClientId());
        verify(orderPersistencePort, times(1)).saveOrder(order);
    }

    @Test
    void createOrder_userIsNotClient_throwsException() {
        clientResponse.getRole().setName(UserRoles.OWNER.toString());  // Set role to something other than CLIENT
        when(jwtService.extractUsername(JWT_TOKEN)).thenReturn(CLIENT_EMAIL);
        when(userFeignClient.findByEmail(CLIENT_EMAIL)).thenReturn(clientResponse);

        UserNotAClientException exception = assertThrows(UserNotAClientException.class, () -> orderUseCase.createOrder(order));

        assertEquals(ErrorMessages.USER_NOT_A_CLIENT.getMessage(CLIENT_EMAIL), exception.getMessage());
    }

    @Test
    void createOrder_userAlreadyHasActiveOrder_throwsException() {
        when(jwtService.extractUsername(JWT_TOKEN)).thenReturn(CLIENT_EMAIL);
        when(userFeignClient.findByEmail(CLIENT_EMAIL)).thenReturn(clientResponse);
        Order activeOrder = new Order(5L, CLIENT_ID, new Date(), OrderStatus.PENDING.toString(), 0L, new HashSet<>(), restaurant);
        when(orderPersistencePort.getByClientId(CLIENT_ID)).thenReturn(List.of(activeOrder));

        UserAlreadyHaveAnOrderActive exception = assertThrows(UserAlreadyHaveAnOrderActive.class, () -> orderUseCase.createOrder(order));

        assertEquals(ErrorMessages.USER_ALREADY_HAS_ACTIVE_ORDER.getMessage(), exception.getMessage());
    }

    @Test
    void appendDish_validClientAndDish_orderUpdated() {
        when(jwtService.extractUsername(JWT_TOKEN)).thenReturn(CLIENT_EMAIL);
        when(userFeignClient.findByEmail(CLIENT_EMAIL)).thenReturn(clientResponse);
        OrderDish orderDish = new OrderDish(order, dish, 1);
        order.getDishes().add(orderDish);

        Order updatedOrder = orderUseCase.appendDish(order, dish, 2);

        assertEquals(1, updatedOrder.getDishes().size());
        assertEquals(3, updatedOrder.getDishes().iterator().next().getAmount()); // Modified to account for Set
        verify(orderDishPersistencePort, times(1)).editOrderDish(anyLong(), any(OrderDish.class));
    }

    @Test
    void appendDish_orderAndDishDifferentRestaurants_throwsException() {
        Dish dishFromAnotherRestaurant = new Dish(10L, "Dish from another restaurant", category, "desc", 20, new Restaurant(20L, "Another Restaurant", "Address", CLIENT_ID, "987654321", "logo-url", "456-NIT"), "url", true);
        when(jwtService.extractUsername(JWT_TOKEN)).thenReturn(CLIENT_EMAIL);
        when(userFeignClient.findByEmail(CLIENT_EMAIL)).thenReturn(clientResponse);

        OrderAndDishNotBelongToTheSameRestaurant exception = assertThrows(OrderAndDishNotBelongToTheSameRestaurant.class, () -> orderUseCase.appendDish(order, dishFromAnotherRestaurant, 2));

        assertEquals(ErrorMessages.ORDER_AND_DISH_NOT_BELONG_TO_SAME_RESTAURANT.getMessage(), exception.getMessage());
    }

    @Test
    void setStatus_validTransition_orderStatusUpdated() {
        when(orderPersistencePort.getById(ORDER_ID)).thenReturn(Optional.of(order));

        Order updatedOrder = orderUseCase.setStatus(ORDER_ID, OrderStatus.IN_PROGRESS.toString());

        assertEquals(OrderStatus.IN_PROGRESS.toString(), updatedOrder.getStatus());
        verify(orderPersistencePort, times(1)).saveOrder(updatedOrder);
    }

    @Test
    void setStatus_invalidTransition_throwsException() {
        order.setStatus(OrderStatus.IN_PROGRESS.toString());
        when(orderPersistencePort.getById(ORDER_ID)).thenReturn(Optional.of(order));

        InvalidOrderStatusTransitionException exception = assertThrows(InvalidOrderStatusTransitionException.class, () -> orderUseCase.setStatus(ORDER_ID, OrderStatus.DELIVERED.toString()));

        assertEquals(ErrorMessages.INVALID_ORDER_STATUS_TRANSITION_EXCEPTION_FROM_TO.getMessage(OrderStatus.IN_PROGRESS.toString(), OrderStatus.DELIVERED.toString()), exception.getMessage());
    }

    @Test
    void assignEmployee_validOrder_employeeAssigned() {
        when(orderPersistencePort.getById(ORDER_ID)).thenReturn(Optional.of(order));
        when(jwtService.extractUsername(JWT_TOKEN)).thenReturn(EMPLOYEE_EMAIL);
        when(userFeignClient.findByEmail(EMPLOYEE_EMAIL)).thenReturn(employeeResponse);
        when(orderPersistencePort.saveOrder(order)).thenReturn(assignedOrder);

        Order simulatedAssignedOrder = orderUseCase.assignEmployee(ORDER_ID);

        assertEquals(employeeResponse.getId(), simulatedAssignedOrder.getChefId());
        assertEquals(OrderStatus.IN_PROGRESS.toString(), simulatedAssignedOrder.getStatus());
        verify(orderPersistencePort, times(1)).saveOrder(order);
    }

    @Test
    void deliverOrder_validCode_orderDelivered() {
        order.setStatus(OrderStatus.READY.toString());
        when(smsPersistencePort.validateCode(ORDER_ID, "123456")).thenReturn(true);

        Order deliveredOrder = orderUseCase.deliverOrder(order, "123456");

        assertEquals(OrderStatus.DELIVERED.toString(), deliveredOrder.getStatus());
        verify(orderPersistencePort, times(1)).saveOrder(deliveredOrder);
    }

}