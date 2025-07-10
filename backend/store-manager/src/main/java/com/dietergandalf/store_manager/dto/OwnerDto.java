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
public class OwnerDto {
    private Long personId;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String phoneNumber;
    private AddressDto address;
    private String email;
    private List<Long> standIds;
    private double totalRent;
    private boolean hasAvailableStands;
}
