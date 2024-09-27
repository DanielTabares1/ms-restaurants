package com.daniel.ms_restaurants.infrastructure.output.jpa.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = EntityConstants.ORDER_TABLE_NAME)
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long clientId;
    private Date date;
    private String status;
    private long chefId;

    @ManyToOne
    private RestaurantEntity restaurant;

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderDishEntity> dishes;

}
