package com.daniel.ms_restaurants.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRestaurantRequest {

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

}
