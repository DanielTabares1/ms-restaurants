package com.daniel.ms_restaurants.domain.api;

import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.model.TraceabilityResponse;
import com.daniel.ms_restaurants.domain.model.enums.OrderStatus;

import java.util.List;

public interface ITraceabilityServicePort {
    List<TraceabilityResponse> getTraceabilityByOrderId(long orderId);
    TraceabilityResponse addTraceability(Order order, String newStatus);
}
