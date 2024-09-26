package com.daniel.ms_restaurants.infrastructure.feignclient;

import com.daniel.ms_restaurants.application.dto.UserResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserFeignClient userFeignClient;

    public CustomUserDetailsService(UserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserResponse userResponse = userFeignClient.findByEmail(email);

        if (userResponse == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Convert UserResponse to UserDetails
        return new org.springframework.security.core.userdetails.User(
                userResponse.getEmail(),
                userResponse.getPassword(),
                mapRolesToAuthorities(List.of(userResponse.getRole().getName()))
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<String> roles) {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}