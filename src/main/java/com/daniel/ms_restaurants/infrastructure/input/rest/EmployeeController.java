package com.daniel.ms_restaurants.infrastructure.input.rest;

import com.daniel.ms_restaurants.application.dto.OrderResponse;
import com.daniel.ms_restaurants.application.handler.IOrderHandler;
import com.daniel.ms_restaurants.application.mapper.IOrderResponseMapper;
import com.daniel.ms_restaurants.domain.model.Order;
import com.daniel.ms_restaurants.infrastructure.input.rest.constants.ApiEndpoints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get orders by status",
            description = "Returns a list of orders filtered by their current status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/orders/by-status")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@RequestParam(value = "status") String status) {
        List<Order> orders = orderHandler.getByStatus(status);
        List<OrderResponse> orderResponseList = orders.stream().map(
                orderResponseMapper::toResponse
        ).toList();
        return new ResponseEntity<>(orderResponseList, HttpStatus.OK);
    }

    @Operation(summary = "Assign employee to order",
            description = "Assigns an employee to an order and returns the updated order details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/orders/assign-order")
    public ResponseEntity<OrderResponse> assignEmployeeToOrder(
            @RequestParam long orderId
    ) {
        Order editedOrder = orderHandler.assignEmployee(orderId);
        return new ResponseEntity<>(orderResponseMapper.toResponse(editedOrder), HttpStatus.OK);
    }

    @Operation(summary = "Edit order status",
            description = "Updates the status of a specific order and returns the updated order details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status provided"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/orders/edit-status")
    public ResponseEntity<OrderResponse> editOrderStatus(@RequestParam long orderId, @RequestParam String statusName) {
        Order editedOrder = orderHandler.editStatus(orderId, statusName);
        return new ResponseEntity<>(orderResponseMapper.toResponse(editedOrder), HttpStatus.OK);
    }

    @Operation(summary = "Deliver an order",
            description = "Validates and marks an order as delivered, then returns the updated order details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order delivered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid validation code"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/orders/deliver-order")
    public ResponseEntity<OrderResponse> deliverOrder(@RequestParam long orderId, @RequestParam String validationCode) {
        Order existingOrder = orderHandler.getById(orderId);
        Order newOrder = orderHandler.deliveryOrder(existingOrder, validationCode);
        return new ResponseEntity<>(orderResponseMapper.toResponse(newOrder), HttpStatus.OK);
    }

}
