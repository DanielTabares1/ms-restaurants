package com.daniel.ms_restaurants.domain.spi;

import com.daniel.ms_restaurants.domain.model.Order;

public interface ISmsPersistencePort {
    void sendSms(String restaurantName, String clientPhoneNumber, String secureCode);
}
