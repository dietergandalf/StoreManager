package com.dietergandalf.store_manager.util;

import com.dietergandalf.store_manager.dto.AddressDto;
import com.dietergandalf.store_manager.dto.RegisterRequestDto;
import com.dietergandalf.store_manager.dto.SellerDto;
import com.dietergandalf.store_manager.dto.UpdateProfileRequestDto;
import com.dietergandalf.store_manager.model.Address;
import com.dietergandalf.store_manager.model.ProductStock;
import com.dietergandalf.store_manager.model.Seller;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SellerMapper {

    public SellerDto toDto(Seller seller) {
        if (seller == null) {
            return null;
        }

        return SellerDto.builder()
                .personId(seller.getPerson_id())
                .firstName(seller.getFirst_name())
                .lastName(seller.getLast_name())
                .dateOfBirth(seller.getDate_of_birth())
                .phoneNumber(seller.getPhone_number())
                .address(toAddressDto(seller.getAddress()))
                .email(seller.getEmail())
                .productStockIds(seller.getProducts() != null ? 
                    seller.getProducts().stream()
                        .map(ProductStock::getProduct_stock_id)
                        .collect(Collectors.toList()) : List.of())
                .standId(seller.getStand() != null ? seller.getStand().getStand_id() : null)
                .build();
    }

    public List<SellerDto> toDtoList(List<Seller> sellers) {
        return sellers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Seller fromRegisterRequest(RegisterRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Seller seller = new Seller();
        seller.setFirst_name(dto.getFirstName());
        seller.setLast_name(dto.getLastName());
        seller.setDate_of_birth(dto.getDateOfBirth());
        seller.setPhone_number(dto.getPhoneNumber());
        seller.setEmail(dto.getEmail());
        seller.setPassword(dto.getPassword()); // Should be hashed in service
        seller.setAddress(fromAddressDto(dto.getAddress()));

        return seller;
    }

    public void updateFromDto(Seller seller, UpdateProfileRequestDto dto) {
        if (dto.getFirstName() != null) {
            seller.setFirst_name(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            seller.setLast_name(dto.getLastName());
        }
        if (dto.getDateOfBirth() != null) {
            seller.setDate_of_birth(dto.getDateOfBirth());
        }
        if (dto.getPhoneNumber() != null) {
            seller.setPhone_number(dto.getPhoneNumber());
        }
        if (dto.getEmail() != null) {
            seller.setEmail(dto.getEmail());
        }
        if (dto.getAddress() != null) {
            seller.setAddress(fromAddressDto(dto.getAddress()));
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

        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setPostalCode(dto.getPostalCode());
        address.setCity(dto.getCity());
        address.setProvince(dto.getProvince());
        address.setCountry(dto.getCountry());

        return address;
    }
}
