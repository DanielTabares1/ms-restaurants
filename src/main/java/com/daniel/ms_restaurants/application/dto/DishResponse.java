package com.daniel.ms_restaurants.application.dto;

import com.daniel.ms_restaurants.domain.model.Category;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishResponse {
    private Long id;
    private String name;
    private String categoryName;
    private String description;
    private int price;
    private String imageUrl;
    private boolean active;
}
