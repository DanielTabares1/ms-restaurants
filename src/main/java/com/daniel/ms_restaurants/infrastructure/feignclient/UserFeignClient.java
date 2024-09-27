package com.daniel.ms_restaurants.infrastructure.feignclient;

import com.daniel.ms_restaurants.application.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-users", url = "http://localhost:8080", configuration = FeignConfig.class)
public interface UserFeignClient {

    @GetMapping("/api/v1/admin/users/{id}")
    UserResponse getUserById(@PathVariable("id") long id);

    @GetMapping("/api/v1/employee/users/{id}")
    UserResponse findUserById(@PathVariable("id") long id);

    @GetMapping("/api/v1/admin/users/by-email/{email}")
    UserResponse getUserByEmail(@PathVariable("email") String email);

    @GetMapping("/api/v1/auth/findByEmail")
    UserResponse findByEmail(@RequestParam("email") String email);


}
