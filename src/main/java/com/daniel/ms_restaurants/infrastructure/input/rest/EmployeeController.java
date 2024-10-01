package com.daniel.ms_restaurants.infrastructure.input.rest;

import com.daniel.ms_restaurants.application.dto.OrderResponse;
import com.daniel.ms_restaurants.application.handler.IOrderHandler;
import com.daniel.ms_restaurants.application.mapper.IOrderResponseMapper;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.infrastructure.input.rest.constants.ApiEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiEndpoints.EMPLOYEE_API)
@RequiredArgsConstructor
public class EmployeeController {

    private final IOrderHandler orderHandler;
    private final IOrderResponseMapper orderResponseMapper;

    @GetMapping("/orders/by-status")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@RequestParam(value = "status") String status) {
        List<Order> orders = orderHandler.getByStatus(status);
        List<OrderResponse> orderResponseList = orders.stream().map(
                orderResponseMapper::toResponse
        ).toList();
        return new ResponseEntity<>(orderResponseList, HttpStatus.OK);
    }

    @PutMapping("/orders/assign-order")
    public ResponseEntity<OrderResponse> assignEmployeeToOrder(
            @RequestParam long orderId
    ) {
        Order editedOrder = orderHandler.assignEmployee(orderId);
        return new ResponseEntity<>(orderResponseMapper.toResponse(editedOrder), HttpStatus.OK);
    }

    @PutMapping("/orders/edit-status")
    public ResponseEntity<OrderResponse> editOrderStatus(@RequestParam long orderId, @RequestParam String statusName) {
        Order editedOrder = orderHandler.editStatus(orderId, statusName);
        return new ResponseEntity<>(orderResponseMapper.toResponse(editedOrder), HttpStatus.OK);
    }

    @PutMapping("/orders/deliver-order")
    public ResponseEntity<OrderResponse> deliverOrder(@RequestParam long orderId, @RequestParam String validationCode) {
        Order existingOrder = orderHandler.getById(orderId);
        Order newOrder = orderHandler.deliveryOrder(existingOrder, validationCode);
        return new ResponseEntity<>(orderResponseMapper.toResponse(newOrder), HttpStatus.OK);
    }

}
