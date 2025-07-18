package com.dietergandalf.store_manager.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.dietergandalf.store_manager.model.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long orderId;
    private Long customerId;
    private String customerName;
    private List<OrderItemDto> orderItems;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
    private String paymentStatus;
    private OrderStatus status;
    private String orderNotes;
}
