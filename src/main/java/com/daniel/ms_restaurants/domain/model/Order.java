package com.daniel.ms_restaurants.domain.model;

import java.util.Date;
import java.util.Set;

public class Order {
    private long id;
    private long clientId;
    private Date date;
    private String status;
    private long chefId;
    private Restaurant restaurant;
    private Set<OrderDish> dishes;


    public Order(){}

    public Order(long id, long clientId, Date date, String status, long chefId, Set<OrderDish> dishes, Restaurant restaurant) {
        this.id = id;
        this.clientId = clientId;
        this.date = date;
        this.status = status;
        this.chefId = chefId;
        this.dishes = dishes;
        this.restaurant = restaurant;
    }

    public Order(long clientId, Date date, String status, long chefId, Set<OrderDish> dishes, Restaurant restaurant) {
        this.clientId = clientId;
        this.date = date;
        this.status = status;
        this.chefId = chefId;
        this.dishes = dishes;
        this.restaurant = restaurant;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getChefId() {
        return chefId;
    }

    public void setChefId(long chefId) {
        this.chefId = chefId;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Set<OrderDish> getDishes() {
        return dishes;
    }

    public void setDishes(Set<OrderDish> dishes) {
        this.dishes = dishes;
    }
}
