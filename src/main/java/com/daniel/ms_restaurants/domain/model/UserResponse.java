package com.daniel.ms_restaurants.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String lastName;
    private String documentNumber;
    private String cellphone;
    private Date birthDate;
    private String email;
    private String password;
    private RoleResponse role;
}
