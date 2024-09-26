package com.daniel.ms_restaurants.domain.api;

import com.daniel.ms_restaurants.application.dto.UserResponse;

public interface IUserClientPort {
    UserResponse getUserById(Long id);

    UserResponse getUserByEmail(String email);
}
