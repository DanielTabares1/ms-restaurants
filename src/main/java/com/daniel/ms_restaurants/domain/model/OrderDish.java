package com.daniel.ms_restaurants.domain.model;

public class OrderDish {
    private long id;
    private Order order;
    private Dish dish;
    private int amount;


    public OrderDish() {
    }

    public OrderDish(long id, Order order, Dish dish, int amount) {
        this.id = id;
        this.order = order;
        this.dish = dish;
        this.amount = amount;
    }

    public OrderDish(Order order, Dish dish, int amount) {
        this.order = order;
        this.dish = dish;
        this.amount = amount;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
