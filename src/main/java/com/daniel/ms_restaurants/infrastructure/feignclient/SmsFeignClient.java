package com.daniel.ms_restaurants.infrastructure.feignclient;

import com.daniel.ms_restaurants.application.dto.OrderMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "ms-messaging", url = "http://localhost:8082", configuration = FeignConfig.class)
public interface SmsFeignClient {

    @PostMapping("/api/v1/employee/sms")
    String sendSms(OrderMessage orderMessage);
    
}
