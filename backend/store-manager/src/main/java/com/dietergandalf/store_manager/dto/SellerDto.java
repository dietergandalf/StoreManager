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
public class SellerDto {
    private Long personId;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String phoneNumber;
    private AddressDto address;
    private String email;
    private List<Long> productStockIds;
    private Long standId;
}
