package com.daniel.ms_restaurants.application.handler;

import com.daniel.ms_restaurants.domain.api.IUserClientPort;
import com.daniel.ms_restaurants.application.dto.RoleResponse;
import com.daniel.ms_restaurants.domain.model.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserHandlerTest {

    @InjectMocks
    private UserHandler userHandler;

    @Mock
    private IUserClientPort userClientPort;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserByID() {
        UserResponse userResponse = new UserResponse(
                7L,
                "Daniel",
                "Tabares",
                "1007480705",
                "+3222574446",
                new Date(2001, Calendar.OCTOBER,13),
                "daniel.tabares@pragma.com.co",
                "$2a$10$rKPEB3J.6k5hpbxWnxQFvuJU8XU0E0OZs9sOhIux08XkiXUnUwfTO",
                new RoleResponse(3l, "ADMIN", "El dueño de todo")
        );

        when(userClientPort.getUserById(7L)).thenReturn(userResponse);

        UserResponse result = userHandler.getUserByID(7L);

        assertNotNull(result);
        assertEquals(userResponse.getId(), result.getId());
        assertEquals(userResponse.getRole(), result.getRole());
        assertEquals(userResponse.getEmail(), result.getEmail());

        verify(userClientPort, times(1)).getUserById(7L);

    }
}