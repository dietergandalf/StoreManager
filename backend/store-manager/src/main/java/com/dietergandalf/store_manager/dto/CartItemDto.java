package com.dietergandalf.store_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    private Long cartItemId;
    private ProductStockDto productStock;
    private Integer quantity;
    private Double priceAtTimeOfAdd;
    private Double totalPrice;
}
