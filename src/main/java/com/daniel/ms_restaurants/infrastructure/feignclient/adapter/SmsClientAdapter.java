package com.daniel.ms_restaurants.infrastructure.feignclient.adapter;

import com.daniel.ms_restaurants.application.dto.OrderMessage;
import com.daniel.ms_restaurants.domain.spi.ISmsPersistencePort;
import com.daniel.ms_restaurants.infrastructure.feignclient.SmsFeignClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SmsClientAdapter implements ISmsPersistencePort {

    private final SmsFeignClient smsFeignClient;

    @Override
    public void sendSms(String restaurantName, String clientPhoneNumber, String secureCode) {
        smsFeignClient.sendSms(new OrderMessage(
                //clientPhoneNumber,
                "+573222574446",
                restaurantName,
                secureCode
        ));
    }
}
