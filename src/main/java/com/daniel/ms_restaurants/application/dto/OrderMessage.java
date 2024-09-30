package com.daniel.ms_restaurants.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderMessage {
    private long orderId;
    private String clientPhoneNumber;
    private String restaurantName;
    private String orderValidationCode;
}
