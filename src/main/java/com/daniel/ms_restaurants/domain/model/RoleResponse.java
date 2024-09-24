package com.daniel.ms_restaurants.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse implements GrantedAuthority {
    private long id;
    private String name;
    private String description;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name;
    }
}
