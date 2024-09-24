package com.daniel.ms_restaurants.infrastructure.security.jwt;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenHolder {

    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();

    public static void setToken(String token) {
        tokenHolder.set(token);
    }

    public static String getToken() {
        return tokenHolder.get();
    }

    public static void clear() {
        tokenHolder.remove();
    }
}
