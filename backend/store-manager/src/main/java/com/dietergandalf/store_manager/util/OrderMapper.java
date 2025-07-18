package com.dietergandalf.store_manager.util;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dietergandalf.store_manager.dto.CheckoutRequestDto;
import com.dietergandalf.store_manager.dto.OrderDto;
import com.dietergandalf.store_manager.dto.OrderItemDto;
import com.dietergandalf.store_manager.model.CartItem;
import com.dietergandalf.store_manager.model.Customer;
import com.dietergandalf.store_manager.model.Order;
import com.dietergandalf.store_manager.model.OrderItem;
import com.dietergandalf.store_manager.model.OrderStatus;

@Component
public class OrderMapper {
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    public OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }
        
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomer().getPerson_id())
                .customerName(order.getCustomer().getFirst_name() + " " + order.getCustomer().getLast_name())
                .orderItems(orderItemMapper.toDtoList(order.getOrderItems()))
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .billingAddress(order.getBillingAddress())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .status(order.getStatus())
                .orderNotes(order.getOrderNotes())
                .build();
    }
    
    public List<OrderDto> toDtoList(List<Order> orders) {
        return orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public Order fromCartItemsAndRequest(Customer customer, List<CartItem> cartItems, CheckoutRequestDto request) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(request.getShippingAddress());
        order.setBillingAddress(request.getBillingAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentStatus("PENDING");
        order.setStatus(OrderStatus.PENDING);
        order.setOrderNotes(request.getOrderNotes());
        
        // Calculate total amount
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPriceAtTimeOfAdd())
                .sum();
        order.setTotalAmount(totalAmount);
        
        return order;
    }
}
