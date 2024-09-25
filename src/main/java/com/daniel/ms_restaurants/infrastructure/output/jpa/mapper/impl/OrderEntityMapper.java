package com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.impl;

import com.daniel.ms_restaurants.domain.model.*;
import com.daniel.ms_restaurants.infrastructure.output.jpa.entity.*;
import com.daniel.ms_restaurants.infrastructure.output.jpa.mapper.IOrderEntityMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderEntityMapper implements IOrderEntityMapper {

    @Override
    public Order toModel(OrderEntity orderEntity) {
        Order order = new Order();
        Restaurant restaurant = getRestaurant(orderEntity);

        order.setId(orderEntity.getId());
        order.setChefId(orderEntity.getChefId());
        order.setClientId(orderEntity.getClientId());
        order.setDate(orderEntity.getDate());
        order.setState(orderEntity.getState());
        order.setRestaurant(restaurant);
        order.setDishes(
                orderEntity.getDishes().stream().map(
                        orderDishEntity -> getDish(orderDishEntity, order, restaurant)
                ).collect(Collectors.toSet())
        );

        return order;
    }

    @Override
    public OrderEntity toEntity(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        RestaurantEntity restaurantEntity = getRestaurantEntity(order);

        orderEntity.setId(order.getId());
        orderEntity.setChefId(order.getChefId());
        orderEntity.setClientId(order.getClientId());
        orderEntity.setDate(order.getDate());
        orderEntity.setState(order.getState());
        orderEntity.setRestaurant(restaurantEntity);
        orderEntity.setDishes(
                order.getDishes().stream().map(
                        orderDish -> getDishEntity(orderDish, orderEntity, restaurantEntity)
                ).collect(Collectors.toSet())
        );

        return orderEntity;
    }

    private OrderDishEntity getDishEntity(OrderDish orderDish, OrderEntity orderEntity, RestaurantEntity restaurant) {
        if (orderDish == null) {
            return null;
        }

        OrderDishEntity orderDishEntity = new OrderDishEntity();
        orderDishEntity.setId(orderDish.getId());
        orderDishEntity.setOrder(orderEntity);
        orderDishEntity.setAmount(orderDish.getAmount());
        orderDishEntity.setDish(getDishEntityFromModel(orderDish.getDish(), restaurant));
        return orderDishEntity;
    }

    private static DishEntity getDishEntityFromModel(Dish dish, RestaurantEntity restaurant) {
        return new DishEntity(
                dish.getId(),
                dish.getName(),
                new CategoryEntity(
                        dish.getCategory().getId(),
                        dish.getCategory().getName(),
                        dish.getCategory().getDescription()
                ),
                dish.getDescription(),
                dish.getPrice(),
                restaurant,
                dish.getImageUrl(),
                dish.isActive()
        );
    }

    private RestaurantEntity getRestaurantEntity(Order order) {

        RestaurantEntity restaurantEntity;
        if (order.getRestaurant() == null) {
            restaurantEntity = null;
        } else {
            restaurantEntity = new RestaurantEntity(
                    order.getRestaurant().getId(),
                    order.getRestaurant().getName(),
                    order.getRestaurant().getAddress(),
                    order.getRestaurant().getOwnerId(),
                    order.getRestaurant().getPhoneNumber(),
                    order.getRestaurant().getLogoUrl(),
                    order.getRestaurant().getNit()
            );
        }
        return restaurantEntity;

    }

    private OrderDish getDish(OrderDishEntity orderDishEntity, Order order, Restaurant restaurant) {
        if (orderDishEntity == null) {
            return null;
        }

        return new OrderDish(
                orderDishEntity.getId(),
                order,
                getDishModelFromEntity(orderDishEntity.getDish(), restaurant),
                orderDishEntity.getAmount()
        );

    }

    private static Dish getDishModelFromEntity(DishEntity dishEntity, Restaurant restaurant) {
        return new Dish(
                dishEntity.getId(),
                dishEntity.getName(),
                new Category(
                        dishEntity.getCategory().getId(),
                        dishEntity.getCategory().getName(),
                        dishEntity.getCategory().getDescription()
                ),
                dishEntity.getDescription(),
                dishEntity.getPrice(),
                restaurant,
                dishEntity.getImageUrl(),
                dishEntity.isActive()
        );
    }

    private Restaurant getRestaurant(OrderEntity orderEntity) {
        Restaurant restaurant;
        if (orderEntity.getRestaurant() == null) {
            restaurant = null;
        } else {
            restaurant = new Restaurant(
                    orderEntity.getRestaurant().getId(),
                    orderEntity.getRestaurant().getName(),
                    orderEntity.getRestaurant().getAddress(),
                    orderEntity.getRestaurant().getOwnerId(),
                    orderEntity.getRestaurant().getPhoneNumber(),
                    orderEntity.getRestaurant().getLogoUrl(),
                    orderEntity.getRestaurant().getNit()
            );
        }
        return restaurant;
    }
}
