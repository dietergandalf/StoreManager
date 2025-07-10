package com.dietergandalf.store_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    private Long personId;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String phoneNumber;
    private AddressDto address;
    private String email;
    // Note: Password is excluded from DTO for security reasons
}
