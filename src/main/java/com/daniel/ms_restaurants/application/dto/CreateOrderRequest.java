package com.daniel.ms_restaurants.application.dto;

import com.daniel.ms_restaurants.domain.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateOrderRequest {
    private long clientId;
    private long chefId;
    private long restaurantId;
}
