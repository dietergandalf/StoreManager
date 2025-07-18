package com.dietergandalf.store_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long orderItemId;
    private Long productStockId;
    private String productName;
    private String productDescription;
    private Double priceAtTimeOfOrder;
    private Integer quantity;
    private Double totalPrice;
}
