package com.dietergandalf.store_manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dietergandalf.store_manager.dto.CheckoutRequestDto;
import com.dietergandalf.store_manager.dto.OrderDto;
import com.dietergandalf.store_manager.model.OrderStatus;
import com.dietergandalf.store_manager.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Order Management", description = "Operations related to order processing and management")
public class OrderController {
    
    private final OrderService orderService;
    
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @Operation(summary = "Checkout and create order", description = "Process checkout and create an order from the customer's shopping cart")
    @PostMapping("/customers/{customerId}/checkout")
    public ResponseEntity<OrderDto> checkout(@PathVariable Long customerId, @RequestBody CheckoutRequestDto checkoutRequest) {
        try {
            OrderDto order = orderService.checkout(customerId, checkoutRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Operation(summary = "Get customer orders", description = "Retrieves all orders for a specific customer")
    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<List<OrderDto>> getCustomerOrders(@PathVariable Long customerId) {
        List<OrderDto> orders = orderService.getCustomerOrders(customerId);
        return ResponseEntity.ok(orders);
    }
    
    @Operation(summary = "Get order by ID", description = "Retrieves a specific order by its unique identifier")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        OrderDto order = orderService.getOrderById(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }
    
    @Operation(summary = "Update order status", description = "Updates the status of an existing order")
    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        try {
            OrderDto updatedOrder = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Operation(summary = "Get all orders", description = "Retrieves all orders in the system (admin functionality)")
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
