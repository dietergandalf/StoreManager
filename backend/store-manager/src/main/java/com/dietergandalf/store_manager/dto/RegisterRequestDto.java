package com.dietergandalf.store_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDto {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String phoneNumber;
    private AddressDto address;
    private String email;
    private String password;
    private String userType; // "customer", "seller", "owner"
}
