package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.application.dto.UserResponse;
import com.daniel.ms_restaurants.domain.api.IJwtServicePort;
import com.daniel.ms_restaurants.domain.api.IOrderServicePort;
import com.daniel.ms_restaurants.domain.exception.*;
import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.model.OrderDish;
import com.daniel.ms_restaurants.domain.model.enums.OrderStatus;
import com.daniel.ms_restaurants.domain.spi.IEmployeeRestaurantPersistencePort;
import com.daniel.ms_restaurants.domain.spi.IOrderDishPersistencePort;
import com.daniel.ms_restaurants.domain.spi.IOrderPersistencePort;
import com.daniel.ms_restaurants.domain.spi.ISmsPersistencePort;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtTokenHolder;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderDishPersistencePort orderDishPersistencePort;
    private final UserFeignClient userFeignClient;
    private final ISmsPersistencePort smsPersistencePort;
    private final IJwtServicePort jwtService;
    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

    private static final SecureRandom random = new SecureRandom();

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IOrderDishPersistencePort orderDishPersistencePort, UserFeignClient userFeignClient, ISmsPersistencePort smsPersistencePort, IJwtServicePort jwtService, IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.orderDishPersistencePort = orderDishPersistencePort;
        this.userFeignClient = userFeignClient;
        this.smsPersistencePort = smsPersistencePort;
        this.jwtService = jwtService;
        this.employeeRestaurantPersistencePort = employeeRestaurantPersistencePort;
    }

    @Override
    public Order createOrder(Order order) {
        UserResponse client = userFeignClient.findByEmail(jwtService.extractUsername(JwtTokenHolder.getToken()));
        order.setClientId(client.getId());
        List<Order> ordersOfClient = orderPersistencePort.getByClientId(client.getId());
        for (Order existingOrder : ordersOfClient) {
            if (existingOrder.getStatus().equals(OrderStatus.PENDING.toString()) ||
                    existingOrder.getStatus().equals(OrderStatus.IN_PROGRESS.toString()) ||
                    existingOrder.getStatus().equals(OrderStatus.READY.toString())
            ) {
                throw new UserAlreadyHaveAnOrderActive(ErrorMessages.USER_ALREADY_HAS_ACTIVE_ORDER.getMessage());
            }
        }
        return orderPersistencePort.saveOrder(order);
    }

    @Override
    public Order appendDish(Order order, Dish dish, int amount) {
        UserResponse client = userFeignClient.findByEmail(jwtService.extractUsername(JwtTokenHolder.getToken()));
        if (client.getId() != order.getClientId()) {
            throw new OrderNotBelongToClientException(ErrorMessages.ORDER_NOT_BELONG_TO_CLIENT.getMessage(order.getId(), client.getUsername()));
        }

        if (order.getRestaurant().getId() != dish.getRestaurant().getId()) {
            throw new OrderAndDishNotBelongToTheSameRestaurant(ErrorMessages.ORDER_AND_DISH_NOT_BELONG_TO_SAME_RESTAURANT.getMessage());
        }

        Optional<OrderDish> existingOrderDish = order.getDishes().stream()
                .filter(orderDish -> Objects.equals(orderDish.getDish().getId(), dish.getId()))
                .findFirst();

        if (existingOrderDish.isPresent()) {
            OrderDish orderDish = existingOrderDish.get();
            orderDish.setAmount(orderDish.getAmount() + amount);
            orderDishPersistencePort.editOrderDish(orderDish.getId(), orderDish);
        } else {
            OrderDish newOrderDish = new OrderDish(order, dish, amount);
            order.getDishes().add(newOrderDish);
            orderPersistencePort.editOrder(order.getId(), order);
        }
        return order;
    }

    @Override
    public Order getById(long orderId) {
        return orderPersistencePort.getById(orderId).orElseThrow(
                () -> new OrderNotFoundException(ErrorMessages.ORDER_NOT_FOUND.getMessage(orderId))
        );
    }

    @Override
    public List<Order> getByClientId(long clientId) {
        return orderPersistencePort.getByClientId(clientId);
    }

    @Override
    public List<Order> getByRestaurantIdAndByStatus(String status) {
        long restaurantId = employeeRestaurantPersistencePort.getByEmployeeEmail(
                jwtService.extractUsername(JwtTokenHolder.getToken())
        ).getId();
        return orderPersistencePort.getByRestaurantIdAndStatus(restaurantId, status);
    }

    @Override
    public Order assignEmployee(long orderId) {
        Order order = orderPersistencePort.getById(orderId).orElseThrow(
                () -> new OrderNotFoundException(ErrorMessages.ORDER_NOT_FOUND.getMessage(orderId))
        );
        UserResponse employee = userFeignClient.findByEmail(jwtService.extractUsername(JwtTokenHolder.getToken()));
        order.setChefId(employee.getId());
        order.setStatus(OrderStatus.IN_PROGRESS.toString());
        return orderPersistencePort.saveOrder(order);
    }

    @Override
    public Order setStatus(long orderId, String status) {
        Order order = orderPersistencePort.getById(orderId).orElseThrow(
                () -> new OrderNotFoundException(ErrorMessages.ORDER_NOT_FOUND.getMessage(orderId))
        );
        validateStatusTransition(order.getStatus(), status);
        order.setStatus(status);
        if (Objects.equals(status, OrderStatus.READY.toString())) {
            smsPersistencePort.sendSms(
                    order.getRestaurant().getName(),
                    userFeignClient.findUserById(order.getClientId()).getName(),
                    (random.nextInt(900000) + 100000) + ""
            );
        }
        return order;
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        switch (currentStatus) {
            case "PENDING":
                if (!newStatus.equals(OrderStatus.CANCELLED.toString()) && !newStatus.equals(OrderStatus.IN_PROGRESS.toString())) {
                    throw new InvalidOrderStatusTransitionException(ErrorMessages.INVALID_ORDER_STATUS_TRANSITION_EXCEPTION_FROM_TO.getMessage(OrderStatus.PENDING.toString(), newStatus));
                }
                break;

            case "IN_PROGRESS":
                if (!newStatus.equals("READY")) {
                    throw new InvalidOrderStatusTransitionException(ErrorMessages.INVALID_ORDER_STATUS_TRANSITION_EXCEPTION_FROM_TO.getMessage(OrderStatus.IN_PROGRESS.toString(), newStatus));
                }
                break;

            case "READY":
                if (!newStatus.equals("DELIVERED")) {
                    throw new InvalidOrderStatusTransitionException(ErrorMessages.INVALID_ORDER_STATUS_TRANSITION_EXCEPTION_FROM_TO.getMessage(OrderStatus.READY.toString(), newStatus));
                }
                break;

            default:
                throw new InvalidOrderStatusTransitionException(ErrorMessages.INVALID_ORDER_STATUS_TRANSITION_EXCEPTION_FROM.getMessage(currentStatus));
        }
    }
}
