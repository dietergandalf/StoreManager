package com.dietergandalf.store_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartDto {
    private Long cartId;
    private Long customerId;
    private String customerName;
    private List<CartItemDto> cartItems;
    private Double totalAmount;
    private Integer totalItems;
}
