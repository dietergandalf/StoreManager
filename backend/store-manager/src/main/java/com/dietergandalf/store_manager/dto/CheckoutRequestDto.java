package com.dietergandalf.store_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutRequestDto {
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
    private String orderNotes;
}
