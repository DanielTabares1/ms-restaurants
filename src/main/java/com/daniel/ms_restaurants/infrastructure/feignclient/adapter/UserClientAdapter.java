package com.daniel.ms_restaurants.infrastructure.feignclient.adapter;

import com.daniel.ms_restaurants.domain.api.IUserClientPort;
import com.daniel.ms_restaurants.domain.model.UserResponse;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserClientAdapter implements IUserClientPort {

    private final UserFeignClient userFeignClient;

    @Override
    public UserResponse getUserById(Long id) {
        return userFeignClient.getUserById(id);
    }
}
