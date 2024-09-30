package com.daniel.ms_restaurants.domain.spi;


public interface ISmsPersistencePort {
    void sendSms(long orderId, String restaurantName, String clientPhoneNumber, String secureCode);
    boolean validateCode(long orderId, String code);
}
