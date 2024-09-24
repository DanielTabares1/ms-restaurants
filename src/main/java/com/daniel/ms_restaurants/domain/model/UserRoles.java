package com.daniel.ms_restaurants.domain.model;

public enum UserRoles {
    ADMIN("ROLE_ADMIN"),
    CLIENT("ROLE_CLIENT"),
    OWNER("ROLE_OWNER"),
    EMPLOYEE("ROLE_EMPLOYEE");

    private final String roleName;

    UserRoles(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return this.roleName;
    }
}
