package com.daniel.ms_restaurants.application.handler.impl;

import com.daniel.ms_restaurants.application.handler.ITraceabilityHandler;
import com.daniel.ms_restaurants.domain.api.IOrderServicePort;
import com.daniel.ms_restaurants.domain.api.ITraceabilityServicePort;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.model.TraceabilityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TraceabilityHandler implements ITraceabilityHandler {

    private final ITraceabilityServicePort traceabilityServicePort;
    private final IOrderServicePort orderServicePort;

    @Override
    public List<TraceabilityResponse> getTraceabilityByOrderId(long orderId) {
        return traceabilityServicePort.getTraceabilityByOrderId(orderId);
    }


    @Override
    public TraceabilityResponse addTraceability(Order order,  String newStatus) {
       return traceabilityServicePort.addTraceability(order, newStatus);
    }

    @Override
    public long getEfficiency(long orderId) {
        return traceabilityServicePort.getEfficiency(orderId);
    }

    @Override
    public String getFormattedEfficiency(long orderId) {
        return traceabilityServicePort.getFormattedEfficiency(orderId);
    }
}
