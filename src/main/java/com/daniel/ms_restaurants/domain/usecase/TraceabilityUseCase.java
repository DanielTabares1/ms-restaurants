package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.application.dto.TraceabilityRequest;
import com.daniel.ms_restaurants.domain.exception.OrderNotEndedException;
import com.daniel.ms_restaurants.domain.model.UserResponse;
import com.daniel.ms_restaurants.domain.api.IJwtServicePort;
import com.daniel.ms_restaurants.domain.api.ITraceabilityServicePort;
import com.daniel.ms_restaurants.domain.exception.ErrorMessages;
import com.daniel.ms_restaurants.domain.exception.OrderNotBelongToClientException;
import com.daniel.ms_restaurants.domain.exception.OrderNotFoundException;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.model.TraceabilityResponse;
import com.daniel.ms_restaurants.domain.model.enums.OrderStatus;
import com.daniel.ms_restaurants.domain.spi.IOrderPersistencePort;
import com.daniel.ms_restaurants.domain.spi.ITraceabilityPersistencePort;
import com.daniel.ms_restaurants.infrastructure.feignclient.UserFeignClient;
import com.daniel.ms_restaurants.infrastructure.security.jwt.JwtTokenHolder;

import java.util.List;

public class TraceabilityUseCase implements ITraceabilityServicePort {

    private final ITraceabilityPersistencePort traceabilityPersistencePort;
    private final UserFeignClient userFeignClient;
    private final IJwtServicePort jwtServicePort;
    private final IOrderPersistencePort orderPersistencePort;

    public TraceabilityUseCase(ITraceabilityPersistencePort traceabilityPersistencePort, IJwtServicePort jwtServicePort, UserFeignClient userFeignClient, IOrderPersistencePort orderPersistencePort) {
        this.traceabilityPersistencePort = traceabilityPersistencePort;
        this.userFeignClient = userFeignClient;
        this.jwtServicePort = jwtServicePort;
        this.orderPersistencePort = orderPersistencePort;
    }

    @Override
    public List<TraceabilityResponse> getTraceabilityByOrderId(long orderId) {
        UserResponse client = userFeignClient.findByEmail(jwtServicePort.extractUsername(JwtTokenHolder.getToken()));
        Order order = orderPersistencePort.getById(orderId).orElseThrow(
                () -> new OrderNotFoundException(ErrorMessages.ORDER_NOT_FOUND.getMessage(orderId))
        );
        if (client.getId() != order.getClientId()) {
            throw new OrderNotBelongToClientException(ErrorMessages.ORDER_NOT_BELONG_TO_CLIENT.getMessage(order.getId(), client.getUsername()));
        }
        return traceabilityPersistencePort.getTraceabilityByOrderId(orderId);
    }

    @Override
    public TraceabilityResponse addTraceability(Order order, String newStatus) {
        UserResponse client = userFeignClient.clientFindUserById(order.getClientId());
        UserResponse employee = userFeignClient.clientFindUserById(order.getClientId());

        TraceabilityRequest traceabilityRequest = new TraceabilityRequest(order, client, employee, newStatus);

        return traceabilityPersistencePort.addTraceability(traceabilityRequest);
    }

    @Override
    public long getEfficiency(long orderId) {
        List<TraceabilityResponse> traceabilityResponseList = traceabilityPersistencePort.getTraceabilityByOrderId(orderId);
        TraceabilityResponse first = traceabilityResponseList.getFirst();
        TraceabilityResponse last = traceabilityResponseList.getLast();

        if (first.getNewState().equals(OrderStatus.PENDING.toString()) &&
                last.getNewState().equals(OrderStatus.DELIVERED.toString())
        ) {
            return last.getDate().getTime() - first.getDate().getTime();
        } else {
            throw new OrderNotEndedException(ErrorMessages.ORDER_NOT_ENDED.getMessage(orderId));
        }
    }

    @Override
    public String getFormattedEfficiency(long orderId) {
        return formatTimeInMillis(getEfficiency(orderId));
    }

    private String formatTimeInMillis(long timeInMillis) {
        long diffInSeconds = timeInMillis / 1000;
        long diffInMinutes = diffInSeconds / 60;
        long diffInHours = diffInMinutes / 60;
        long diffInDays = diffInHours / 24;

        long remainingHours = diffInHours % 24;
        long remainingMinutes = diffInMinutes % 60;
        long remainingSeconds = diffInSeconds % 60;

        return diffInDays + " Days, " + remainingHours + " Hours, " +
                remainingMinutes + " Minutes, " + remainingSeconds + " Seconds";
    }

}
