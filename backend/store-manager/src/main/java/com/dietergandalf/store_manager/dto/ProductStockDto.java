package com.dietergandalf.store_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStockDto {
    private Long productStockId;
    private ProductDto product;
    private Long sellerId;
    private String sellerName;
    private Integer amount;
}
