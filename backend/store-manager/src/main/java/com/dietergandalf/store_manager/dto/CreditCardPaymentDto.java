package com.dietergandalf.store_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditCardPaymentDto {
    private Long creditCardId;
    private String cardNumber; // This should be masked in real implementation
    private String expiryDate;
    private Long cardHolderId;
    private String cardHolderName;
    private String type;
}
