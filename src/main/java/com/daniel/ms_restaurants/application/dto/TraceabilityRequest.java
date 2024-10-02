package com.daniel.ms_restaurants.application.dto;

import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.model.UserResponse;
import com.daniel.ms_restaurants.domain.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraceabilityRequest {
    private long orderId;
    private long clientId;
    private String clientEmail;
    private Date date;
    private String previousState;
    private String newState;
    private long employeeId;
    private String employeeEmail;

    public TraceabilityRequest(Order order, UserResponse client, UserResponse employee, String newState) {
        this.orderId=order.getId();
        this.clientId = client.getId();
        this.clientEmail = client.getEmail();
        this.date = new Date();
        if (!Objects.equals(newState, OrderStatus.PENDING.toString())) {
            this.previousState = order.getStatus();
        }
        this.newState = newState;
        if (employee != null) {
            this.employeeId = employee.getId();
            this.employeeEmail = employee.getEmail();
        }
    }

}
