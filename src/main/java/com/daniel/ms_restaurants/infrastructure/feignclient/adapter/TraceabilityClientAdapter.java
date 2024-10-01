package com.daniel.ms_restaurants.infrastructure.feignclient.adapter;

import com.daniel.ms_restaurants.application.dto.TraceabilityRequest;
import com.daniel.ms_restaurants.domain.model.TraceabilityResponse;
import com.daniel.ms_restaurants.domain.spi.ITraceabilityPersistencePort;
import com.daniel.ms_restaurants.infrastructure.feignclient.TraceabilityFeignClient;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TraceabilityClientAdapter implements ITraceabilityPersistencePort {
    private final TraceabilityFeignClient traceabilityFeignClient;


    @Override
    public TraceabilityResponse addTraceability(TraceabilityRequest traceability) {
        return traceabilityFeignClient.addTraceability(traceability);
    }

    @Override
    public List<TraceabilityResponse> getTraceabilityByOrderId(long orderId) {
        return traceabilityFeignClient.getByOrderId(orderId);
    }

}
