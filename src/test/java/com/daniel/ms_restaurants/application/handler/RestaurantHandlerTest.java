package com.daniel.ms_restaurants.application.handler;

import com.daniel.ms_restaurants.application.handler.impl.RestaurantHandler;
import com.daniel.ms_restaurants.domain.api.IRestaurantServicePort;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RestaurantHandlerTest {

    @InjectMocks
    private RestaurantHandler restaurantHandler;

    @Mock
    private IRestaurantServicePort IRestaurantServicePort;

    @Mock
    private UserFeignClient userFeignClient;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveRestaurant() {

    }

    @Test
    void saveRestaurant_FailsWhenOwnerNotFound() {



    }

    @Test
    void saveRestaurant_FailsWhenUserIsNotOwner() {

    }


}