package com.daniel.ms_restaurants.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDishRequest {
    @NotBlank(message = "Dish name cannot be blank")
    private String name;

    @NotNull(message = "Dish price cannot be null")
    @Positive(message = "Dish price must be greater than zero")
    private Integer price;

    @NotBlank(message = "Dish description cannot be blank")
    private String description;

    @NotBlank(message = "Image URL cannot be blank")
    private String imageUrl;

    @NotNull(message = "Category ID cannot be null")
    private Long idCategory;

    @NotNull
    private Long idRestaurant;
}
