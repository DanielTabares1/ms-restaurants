package com.daniel.ms_restaurants.infrastructure.feignclient;


import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtTokenHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String jwtToken = JwtTokenHolder.getToken();

        if (jwtToken != null) {
            template.header("Authorization", "Bearer " + jwtToken);
        }
    }
}

