package com.daniel.ms_restaurants.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Restaurant {

    private long id;

    @NotBlank
    @Size(min = 1, message = "Restaurant name could ha")
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private long ownerId;

    @NotBlank
    @Pattern(regexp = "^\\+?\\d{10,12}$", message = "Phone must be numeric")
    private String phoneNumber;

    @NotBlank
    private String logoUrl;

    @NotBlank
    @Pattern(regexp = "\\d+", message = "Nit must be numeric value")
    private String nit;


    public Restaurant() {
    }

    public Restaurant(long id, String name, String address, long ownerId, String phoneNumber, String logoUrl, String nit) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.ownerId = ownerId;
        this.phoneNumber = phoneNumber;
        this.logoUrl = logoUrl;
        this.nit = nit;
    }

    public Restaurant(String address, String name, long ownerId, String phoneNumber, String logoUrl, String nit) {
        this.address = address;
        this.name = name;
        this.ownerId = ownerId;
        this.phoneNumber = phoneNumber;
        this.logoUrl = logoUrl;
        this.nit = nit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }
}
