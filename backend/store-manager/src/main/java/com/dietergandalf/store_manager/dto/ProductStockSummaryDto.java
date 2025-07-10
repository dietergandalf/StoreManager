package com.dietergandalf.store_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStockSummaryDto {
    private Long productStockId;
    private Long productId;
    private String productName;
    private Double productPrice;
    private Integer amount;
    private Long sellerId;
    private String sellerName;
}
