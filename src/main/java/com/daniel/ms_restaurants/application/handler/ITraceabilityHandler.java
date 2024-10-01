package com.daniel.ms_restaurants.application.handler;

import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.model.TraceabilityResponse;

import java.util.List;

public interface ITraceabilityHandler {
    List<TraceabilityResponse> getTraceabilityByOrderId(long orderId);
    TraceabilityResponse addTraceability(Order order, String newStatus);
}
