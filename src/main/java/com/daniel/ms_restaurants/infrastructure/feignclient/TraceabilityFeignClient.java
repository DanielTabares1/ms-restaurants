package com.daniel.ms_restaurants.infrastructure.feignclient;

import com.daniel.ms_restaurants.application.dto.TraceabilityRequest;
import com.daniel.ms_restaurants.domain.model.TraceabilityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ms-traceability", url = "http://localhost:8083")
public interface TraceabilityFeignClient {

    @PostMapping("/api/v1/traceability")
    TraceabilityResponse addTraceability(@RequestBody TraceabilityRequest traceabilityRequest);

    @GetMapping("/api/v1/traceability/by-order-id/{orderId}")
    List<TraceabilityResponse> getByOrderId(@PathVariable long orderId);

}
