package com.daniel.ms_restaurants.application.handler;

import com.daniel.ms_restaurants.domain.api.IUserClientPort;
import com.daniel.ms_restaurants.application.dto.UserResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserHandler {

    private final IUserClientPort userClientPort;

    public UserResponse getUserByID(Long id) {
        return userClientPort.getUserById(id);
    }
}
