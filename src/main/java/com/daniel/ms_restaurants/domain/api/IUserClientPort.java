package com.daniel.ms_restaurants.domain.api;

import com.daniel.ms_restaurants.domain.model.UserResponse;

public interface IUserClientPort {
    UserResponse getUserById(Long id);
}
