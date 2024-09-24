package com.daniel.ms_restaurants.domain.api;

import com.daniel.ms_restaurants.domain.model.UserResponse;

import java.util.Optional;

public interface IUserClientPort {
    UserResponse getUserById(Long id);

    UserResponse getUserByEmail(String email);
}
