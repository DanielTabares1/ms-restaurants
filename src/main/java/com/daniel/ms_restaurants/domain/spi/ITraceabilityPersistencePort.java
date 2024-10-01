package com.daniel.ms_restaurants.domain.spi;

import com.daniel.ms_restaurants.application.dto.TraceabilityRequest;
import com.daniel.ms_restaurants.domain.model.TraceabilityResponse;

import java.util.List;

public interface ITraceabilityPersistencePort {
    TraceabilityResponse addTraceability(TraceabilityRequest traceabilityRequest);
    List<TraceabilityResponse> getTraceabilityByOrderId(long orderId);
}
