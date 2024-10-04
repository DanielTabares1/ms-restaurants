package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.domain.api.IJwtServicePort;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.model.Restaurant;
import com.daniel.ms_restaurants.domain.model.UserResponse;
import com.daniel.ms_restaurants.domain.spi.IOrderPersistencePort;
import com.daniel.ms_restaurants.domain.spi.ITraceabilityPersistencePort;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TraceabilityUseCaseTest {
    @InjectMocks
    private TraceabilityUseCase traceabilityUseCase;

    @Mock
    private ITraceabilityPersistencePort traceabilityPersistencePort;
    @Mock
    private UserFeignClient userFeignClient;
    @Mock
    private IJwtServicePort jwtServicePort;
    @Mock
    private IOrderPersistencePort orderPersistencePort;


    private Order testOrder;
    private UserResponse testClient;
    private UserResponse testEmployee;



    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testGetTraceabilityByOrderId_WhenOrderNotExist_ShouldThrowException(){

    }

    @Test
    void testGetTraceabilityByOrderId_WhenOrderIsNotOfTheClient_ShouldThrowException(){

    }

    @Test
    void testGetTraceabilityByOrderId_WhenOrderBelongsToClient_ShouldReturnTraceability(){

    }


    @Test
    void testAddTraceability_ShouldAddNewTraceability(){

    }


    @Test
    void testGetEfficiency_WhenOrderIsNotDelivered_ShouldThrowException(){

    }

    @Test
    void testGetEfficiency_WhenOrderIsDelivered_ShouldReturnEfficiency(){

    }

    @Test
    void getFormattedEfficiency_ShouldReturnFormattedEfficiency(){

    }


}