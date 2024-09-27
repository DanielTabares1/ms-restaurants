package com.daniel.ms_restaurants.application.mapper;

import com.daniel.ms_restaurants.application.dto.OrderResponse;
import com.daniel.ms_restaurants.domain.model.Order;

public interface IOrderResponseMapper {
    OrderResponse toResponse(Order order);
}
