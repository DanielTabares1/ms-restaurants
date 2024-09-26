package com.daniel.ms_restaurants.infrastructure.input.rest;

import com.daniel.ms_restaurants.application.dto.OrderResponse;
import com.daniel.ms_restaurants.application.handler.IOrderHandler;
import com.daniel.ms_restaurants.application.mapper.IOrderResponseMapper;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.domain.spi.IOrderPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final IOrderHandler orderHandler;
    private final IOrderResponseMapper orderResponseMapper;

    @GetMapping("/by-status")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@RequestParam(value = "status") String status){
       List<Order> orders = orderHandler.getByStatus(status);
       List<OrderResponse> orderResponseList = orders.stream().map(
               orderResponseMapper::toResponse
       ).toList();
       return new ResponseEntity<>(orderResponseList, HttpStatus.OK);
    }


}
