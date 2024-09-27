package com.daniel.ms_restaurants.application.mapper.impl;

import com.daniel.ms_restaurants.application.dto.DishResponse;
import com.daniel.ms_restaurants.application.dto.OrderDishResponse;
import com.daniel.ms_restaurants.application.dto.OrderResponse;
import com.daniel.ms_restaurants.application.dto.RestaurantResponse;
import com.daniel.ms_restaurants.application.mapper.IOrderResponseMapper;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.model.OrderDish;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderResponseMapper implements IOrderResponseMapper {
    @Override
    public OrderResponse toResponse(Order order) {

        List<OrderDishResponse> dishes = new ArrayList<>();

        for (OrderDish orderDish : order.getDishes()) {
            dishes.add(
                    new OrderDishResponse(
                            new DishResponse(
                                    orderDish.getDish().getId(),
                                    orderDish.getDish().getName(),
                                    orderDish.getDish().getCategory().getName(),
                                    orderDish.getDish().getDescription(),
                                    orderDish.getDish().getPrice(),
                                    orderDish.getDish().getImageUrl(),
                                    orderDish.getDish().isActive()
                            ),
                            orderDish.getAmount()
                    )

            );
        }

        return new OrderResponse(
                order.getId(),
                new RestaurantResponse(
                        order.getRestaurant().getName(),
                        order.getRestaurant().getLogoUrl()
                ),
                dishes
        );
    }
}
