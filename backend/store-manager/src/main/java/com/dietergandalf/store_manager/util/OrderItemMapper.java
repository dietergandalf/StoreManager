package com.dietergandalf.store_manager.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.dietergandalf.store_manager.dto.OrderItemDto;
import com.dietergandalf.store_manager.model.CartItem;
import com.dietergandalf.store_manager.model.Order;
import com.dietergandalf.store_manager.model.OrderItem;

@Component
public class OrderItemMapper {
    
    public OrderItemDto toDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        
        return OrderItemDto.builder()
                .orderItemId(orderItem.getOrderItemId())
                .productStockId(orderItem.getProductStock().getProduct_stock_id())
                .productName(orderItem.getProductStock().getProduct().getName())
                .productDescription(orderItem.getProductStock().getProduct().getDescription())
                .priceAtTimeOfOrder(orderItem.getPriceAtTimeOfOrder())
                .quantity(orderItem.getQuantity())
                .totalPrice(orderItem.getQuantity() * orderItem.getPriceAtTimeOfOrder())
                .build();
    }
    
    public List<OrderItemDto> toDtoList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public OrderItem fromCartItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProductStock(cartItem.getProductStock());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPriceAtTimeOfOrder(cartItem.getPriceAtTimeOfAdd());
        return orderItem;
    }
    
    public List<OrderItem> fromCartItems(List<CartItem> cartItems, Order order) {
        return cartItems.stream()
                .map(cartItem -> fromCartItem(cartItem, order))
                .collect(Collectors.toList());
    }
}
