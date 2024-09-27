package com.daniel.ms_restaurants.domain.model;

public class EmployeeRestaurant {
    private long id;
    private long restaurantId;
    private String employeeEmail;


    public EmployeeRestaurant() {
    }

    public EmployeeRestaurant(long id, long restaurantId, String employeeEmail) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.employeeEmail = employeeEmail;
    }

    public EmployeeRestaurant(String employeeEmail, long restaurantId) {
        this.employeeEmail = employeeEmail;
        this.restaurantId = restaurantId;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }
}
