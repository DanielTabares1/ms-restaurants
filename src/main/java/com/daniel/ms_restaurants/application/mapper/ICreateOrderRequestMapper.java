package com.daniel.ms_restaurants.application.mapper;

import com.daniel.ms_restaurants.application.dto.CreateOrderRequest;
import com.daniel.ms_restaurants.domain.model.Order;

public interface ICreateOrderRequestMapper {
    Order toModel(CreateOrderRequest request);
}
