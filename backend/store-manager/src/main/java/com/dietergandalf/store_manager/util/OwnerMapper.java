package com.dietergandalf.store_manager.util;

import com.dietergandalf.store_manager.dto.AddressDto;
import com.dietergandalf.store_manager.dto.OwnerDto;
import com.dietergandalf.store_manager.dto.RegisterRequestDto;
import com.dietergandalf.store_manager.dto.UpdateProfileRequestDto;
import com.dietergandalf.store_manager.model.Address;
import com.dietergandalf.store_manager.model.Owner;
import com.dietergandalf.store_manager.model.Stand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OwnerMapper {

    public OwnerDto toDto(Owner owner) {
        if (owner == null) {
            return null;
        }

        return OwnerDto.builder()
                .personId(owner.getPerson_id())
                .firstName(owner.getFirst_name())
                .lastName(owner.getLast_name())
                .dateOfBirth(owner.getDate_of_birth())
                .phoneNumber(owner.getPhone_number())
                .address(toAddressDto(owner.getAddress()))
                .email(owner.getEmail())
                .standIds(owner.getStands() != null ? 
                    owner.getStands().stream()
                        .map(Stand::getStand_id)
                        .collect(Collectors.toList()) : List.of())
                .totalRent(owner.getRent())
                .hasAvailableStands(owner.hasAvailableStand())
                .build();
    }

    public List<OwnerDto> toDtoList(List<Owner> owners) {
        return owners.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Owner fromRegisterRequest(RegisterRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Owner owner = new Owner();
        owner.setFirst_name(dto.getFirstName());
        owner.setLast_name(dto.getLastName());
        owner.setDate_of_birth(dto.getDateOfBirth());
        owner.setPhone_number(dto.getPhoneNumber());
        owner.setAddress(fromAddressDto(dto.getAddress()));
        owner.setEmail(dto.getEmail());
        owner.setPassword(dto.getPassword()); // In real app, this should be hashed
        return owner;
    }

    public void updateOwnerFromDto(Owner owner, UpdateProfileRequestDto dto) {
        if (dto == null || owner == null) {
            return;
        }

        if (dto.getFirstName() != null) {
            owner.setFirst_name(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            owner.setLast_name(dto.getLastName());
        }
        if (dto.getDateOfBirth() != null) {
            owner.setDate_of_birth(dto.getDateOfBirth());
        }
        if (dto.getPhoneNumber() != null) {
            owner.setPhone_number(dto.getPhoneNumber());
        }
        if (dto.getAddress() != null) {
            owner.setAddress(fromAddressDto(dto.getAddress()));
        }
        if (dto.getEmail() != null) {
            owner.setEmail(dto.getEmail());
        }
    }

    private AddressDto toAddressDto(Address address) {
        if (address == null) {
            return null;
        }

        return AddressDto.builder()
                .street(address.getStreet())
                .postalCode(address.getPostalCode())
                .city(address.getCity())
                .province(address.getProvince())
                .country(address.getCountry())
                .build();
    }

    private Address fromAddressDto(AddressDto dto) {
        if (dto == null) {
            return null;
        }

        return Address.builder()
                .street(dto.getStreet())
                .postalCode(dto.getPostalCode())
                .city(dto.getCity())
                .province(dto.getProvince())
                .country(dto.getCountry())
                .build();
    }
}
