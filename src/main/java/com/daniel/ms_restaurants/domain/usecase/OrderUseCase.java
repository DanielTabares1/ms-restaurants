package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.application.dto.TraceabilityRequest;
import com.daniel.ms_restaurants.domain.model.*;
import com.daniel.ms_restaurants.domain.api.IJwtServicePort;
import com.daniel.ms_restaurants.domain.api.IOrderServicePort;
import com.daniel.ms_restaurants.domain.exception.*;
import com.daniel.ms_restaurants.domain.model.enums.OrderStatus;
import com.daniel.ms_restaurants.domain.model.enums.UserRoles;
import com.daniel.ms_restaurants.domain.spi.*;
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
    private final ITraceabilityPersistencePort traceabilityPersistencePort;

    private static final SecureRandom random = new SecureRandom();

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IOrderDishPersistencePort orderDishPersistencePort, UserFeignClient userFeignClient, ISmsPersistencePort smsPersistencePort, IJwtServicePort jwtService, IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort, ITraceabilityPersistencePort traceabilityPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.orderDishPersistencePort = orderDishPersistencePort;
        this.userFeignClient = userFeignClient;
        this.smsPersistencePort = smsPersistencePort;
        this.jwtService = jwtService;
        this.employeeRestaurantPersistencePort = employeeRestaurantPersistencePort;
        this.traceabilityPersistencePort = traceabilityPersistencePort;
    }

    @Override
    public Order createOrder(Order order) {
        UserResponse client = userFeignClient.findByEmail(jwtService.extractUsername(JwtTokenHolder.getToken()));
        if (!Objects.equals(client.getRole().getName(), UserRoles.CLIENT.toString())) {
            throw new UserNotAClientException(ErrorMessages.USER_NOT_A_CLIENT.getMessage(client.getEmail()));
        }
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
        Order newOrder = orderPersistencePort.saveOrder(order);
        traceabilityPersistencePort.addTraceability(
                new TraceabilityRequest(newOrder, client, null, OrderStatus.PENDING.toString())
        );
        return newOrder;
    }

    @Override
    public Order appendDish(Order order, Dish dish, int amount) {
        UserResponse client = userFeignClient.findByEmail(jwtService.extractUsername(JwtTokenHolder.getToken()));
        if (!Objects.equals(client.getRole().getName(), UserRoles.CLIENT.toString())) {
            throw new UserNotAClientException(ErrorMessages.USER_NOT_A_CLIENT.getMessage(client.getEmail()));
        }

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
        String employeeEmail = jwtService.extractUsername(JwtTokenHolder.getToken());
        long restaurantId = employeeRestaurantPersistencePort.getByEmployeeEmail(employeeEmail).getId();
        return orderPersistencePort.getByRestaurantIdAndStatus(restaurantId, status);
    }

    @Override
    public Order assignEmployee(long orderId) {
        Order order = orderPersistencePort.getById(orderId).orElseThrow(
                () -> new OrderNotFoundException(ErrorMessages.ORDER_NOT_FOUND.getMessage(orderId))
        );
        UserResponse employee = userFeignClient.findByEmail(jwtService.extractUsername(JwtTokenHolder.getToken()));
        UserResponse client = userFeignClient.employeeFindUserById(order.getClientId());
        order.setChefId(employee.getId());
        traceabilityPersistencePort.addTraceability(new TraceabilityRequest(
                order, client, employee, OrderStatus.IN_PROGRESS.toString()
        ));
        order.setStatus(OrderStatus.IN_PROGRESS.toString());
        return orderPersistencePort.saveOrder(order);
    }

    @Override
    public Order setStatus(long orderId, String status) {
        Order order = orderPersistencePort.getById(orderId).orElseThrow(
                () -> new OrderNotFoundException(ErrorMessages.ORDER_NOT_FOUND.getMessage(orderId))
        );
        validateStatusTransition(order.getStatus(), status);

        UserResponse client = userFeignClient.employeeFindUserById(order.getClientId());
        UserResponse employee = userFeignClient.employeeFindUserById(order.getChefId());


        traceabilityPersistencePort.addTraceability(
                new TraceabilityRequest(order, client, employee, OrderStatus.READY.toString())
        );

        order.setStatus(status);

        if (Objects.equals(status, OrderStatus.READY.toString())) {
            smsPersistencePort.sendSms(
                    orderId,
                    order.getRestaurant().getName(),
                    userFeignClient.employeeFindUserById(order.getClientId()).getName(),
                    (random.nextInt(900000) + 100000) + ""
            );
        }
        orderPersistencePort.saveOrder(order);
        return order;
    }

    @Override
    public Order deliverOrder(Order order, String code) {
        if (!smsPersistencePort.validateCode(order.getId(), code)) {
            throw new OrderDeliveryValidationCodeException(ErrorMessages.ORDER_DELIVERY_VALIDATION_CODE.getMessage());
        }
        validateStatusTransition(order.getStatus(), OrderStatus.DELIVERED.toString());

        UserResponse client = userFeignClient.employeeFindUserById(order.getClientId());
        UserResponse employee = userFeignClient.employeeFindUserById(order.getChefId());

        traceabilityPersistencePort.addTraceability(
                new TraceabilityRequest(order, client, employee, OrderStatus.DELIVERED.toString())
        );

        order.setStatus(OrderStatus.DELIVERED.toString());
        orderPersistencePort.saveOrder(order);
        return order;
    }

    @Override
    public Order cancellOrder(Order order) {
        UserResponse client = userFeignClient.findByEmail(jwtService.extractUsername(JwtTokenHolder.getToken()));
        UserResponse employee = userFeignClient.clientFindUserById(order.getChefId());
        if (client.getId() != order.getClientId()) {
            throw new OrderNotBelongToClientException(ErrorMessages.ORDER_NOT_BELONG_TO_CLIENT.getMessage(order.getId(), client.getUsername()));
        }
        validateStatusTransition(order.getStatus(), OrderStatus.CANCELLED.toString());
        traceabilityPersistencePort.addTraceability(
                new TraceabilityRequest(order, client, employee, OrderStatus.CANCELLED.toString())
        );
        order.setStatus(OrderStatus.CANCELLED.toString());
        orderPersistencePort.saveOrder(order);
        return order;
    }

    @Override
    public long getEfficiencyOfEmployee(long employeeId) {
        List<Order> orderList = orderPersistencePort.getAllByEmployeeIdByStatus(employeeId, OrderStatus.DELIVERED.toString());

        if (orderList.isEmpty()) {
            return 0;
        }

        long sumOfTime = orderList.stream()
                .mapToLong(this::getDeliveryTime)
                .sum();

        return sumOfTime / orderList.size();
    }

    @Override
    public String getFormattedEfficiencyOfEmployee(long employeeId) {
        return formatTimeInMillis(getEfficiencyOfEmployee(employeeId));
    }

    private String formatTimeInMillis(long timeInMillis) {
        long diffInSeconds = timeInMillis / 1000;
        long diffInMinutes = diffInSeconds / 60;
        long diffInHours = diffInMinutes / 60;
        long diffInDays = diffInHours / 24;

        return diffInDays + " Days, " + diffInHours + " Hours, " +
                diffInMinutes + " Minutes, " + diffInSeconds + " Seconds";
    }


    private long getDeliveryTime(Order order) {
        List<TraceabilityResponse> traceabilityResponseList = traceabilityPersistencePort.getTraceabilityByOrderId(order.getId());
        TraceabilityResponse first = traceabilityResponseList.getFirst();
        TraceabilityResponse last = traceabilityResponseList.getLast();

        if (first.getNewState().equals(OrderStatus.PENDING.toString()) &&
                last.getNewState().equals(OrderStatus.DELIVERED.toString())
        ) {
            return last.getDate().getTime() - first.getDate().getTime();
        }
        return 0;
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
