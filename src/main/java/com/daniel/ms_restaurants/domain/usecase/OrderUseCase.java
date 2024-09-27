package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.application.dto.UserResponse;
import com.daniel.ms_restaurants.domain.api.IJwtServicePort;
import com.daniel.ms_restaurants.domain.api.IOrderServicePort;
import com.daniel.ms_restaurants.domain.exception.OrderAndDishNotBelongToTheSameRestaurant;
import com.daniel.ms_restaurants.domain.exception.OrderNotBelongToClientException;
import com.daniel.ms_restaurants.domain.exception.OrderNotFoundException;
import com.daniel.ms_restaurants.domain.exception.UserAlreadyHaveAnOrderActive;
import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.model.OrderDish;
import com.daniel.ms_restaurants.domain.model.enums.OrderStatus;
import com.daniel.ms_restaurants.domain.spi.IEmployeeRestaurantPersistencePort;
import com.daniel.ms_restaurants.domain.spi.IOrderDishPersistencePort;
import com.daniel.ms_restaurants.domain.spi.IOrderPersistencePort;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtTokenHolder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderDishPersistencePort orderDishPersistencePort;
    private final UserFeignClient userFeignClient;
    private final IJwtServicePort jwtService;
    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IOrderDishPersistencePort orderDishPersistencePort, UserFeignClient userFeignClient, IJwtServicePort jwtService, IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.orderDishPersistencePort = orderDishPersistencePort;
        this.userFeignClient = userFeignClient;
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
                throw new UserAlreadyHaveAnOrderActive("The client already has an order in progress");
            }
        }
        return orderPersistencePort.saveOrder(order);
    }

    @Override
    public Order appendDish(Order order, Dish dish, int amount) {
        UserResponse client = userFeignClient.findByEmail(jwtService.extractUsername(JwtTokenHolder.getToken()));
        if (client.getId() != order.getClientId()) {
            throw new OrderNotBelongToClientException("Order with id: " + order.getClientId() + "not belongs to client with email: " + client.getUsername());
        }

        if (order.getRestaurant().getId() != dish.getRestaurant().getId()) {
            throw new OrderAndDishNotBelongToTheSameRestaurant("The order and the dish not belongs to the same restaurant");
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
                () -> new OrderNotFoundException("Order not found with id: " + orderId)
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
}
