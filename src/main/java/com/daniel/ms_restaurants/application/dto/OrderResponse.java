package com.daniel.ms_restaurants.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private long id;
    private RestaurantResponse restaurantResponse;
    private List<OrderDishResponse> dishes;
}
