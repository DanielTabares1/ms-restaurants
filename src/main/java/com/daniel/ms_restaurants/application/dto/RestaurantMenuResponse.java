package com.daniel.ms_restaurants.application.dto;

import com.daniel.ms_restaurants.domain.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantMenuResponse {
    private Restaurant restaurant;
    List<DishResponse> dishList;
}
