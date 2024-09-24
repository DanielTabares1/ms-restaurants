package com.daniel.ms_restaurants.domain.model;

public enum OrderStatus {
    PENDING("PENDING"),
    IN_PROGRESS("IN_PROGRESS"),
    READY("READY"),
    CANCELLED("CANCELLED"),
    DELIVERED("DELIVERED");

    private final String statusName;

    OrderStatus(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return this.statusName;
    }
}
