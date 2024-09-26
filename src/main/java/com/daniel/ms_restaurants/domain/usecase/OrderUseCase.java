package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.domain.api.IOrderServicePort;
import com.daniel.ms_restaurants.domain.model.Dish;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.model.OrderDish;
import com.daniel.ms_restaurants.domain.spi.IOrderDishPersistencePort;
import com.daniel.ms_restaurants.domain.spi.IOrderPersistencePort;

import java.util.List;
import java.util.Optional;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderDishPersistencePort orderDishPersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IOrderDishPersistencePort orderDishPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.orderDishPersistencePort = orderDishPersistencePort;
    }

    @Override
    public Order createOrder(Order order) {
        return orderPersistencePort.createOrder(order);
    }

    @Override
    public Order appendDish(Order order, Dish dish, int amount) {
        Optional<OrderDish> existingOrderDish = order.getDishes().stream()
                .filter(orderDish -> orderDish.getDish().getId() == dish.getId())
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
        return orderPersistencePort.getById(orderId);
    }

    @Override
    public List<Order> getByClientId(long clientId) {
        return orderPersistencePort.getByClientId(clientId);
    }

    @Override
    public List<Order> getByRestaurantIdAndByStatus(long restaurantId, String status) {
        return orderPersistencePort.getByRestaurantIdAndStatus(restaurantId, status);
    }
}
