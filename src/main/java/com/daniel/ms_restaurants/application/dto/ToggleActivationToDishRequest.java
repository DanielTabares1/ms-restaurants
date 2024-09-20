package com.daniel.ms_restaurants.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ToggleActivationToDishRequest {
    private long dishId;
    private boolean activate;
}
