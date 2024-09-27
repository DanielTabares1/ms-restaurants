package com.daniel.ms_restaurants.infrastructure.output.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = EntityConstants.DISH_TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    private CategoryEntity category;
    private String description;
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    private RestaurantEntity restaurant;
    private String imageUrl;
    private boolean active;
}
