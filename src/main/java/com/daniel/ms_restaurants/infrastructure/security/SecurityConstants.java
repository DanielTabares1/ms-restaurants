package com.daniel.ms_restaurants.infrastructure.security;

public final class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/api/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/docs/**",
    };

    public static final String ADMIN_API = "/api/v1/admin/**";
    public static final String OWNER_API = "/api/v1/owner/**";
    public static final String CLIENT_API = "/api/v1/client/**";

    public static final String ADMIN_ROLE = "ADMIN";
    public static final String OWNER_ROLE = "OWNER";
    public static final String CLIENT_ROLE = "CLIENT";

    public static final String ROLE_NAME_CLAIM = "roleName";

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String AUTHENTICATION_SCHEME = "Bearer ";

}
