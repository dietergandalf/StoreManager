package com.dietergandalf.store_manager.util;

import com.dietergandalf.store_manager.dto.AddressDto;
import com.dietergandalf.store_manager.dto.CustomerDto;
import com.dietergandalf.store_manager.dto.RegisterRequestDto;
import com.dietergandalf.store_manager.dto.UpdateProfileRequestDto;
import com.dietergandalf.store_manager.model.Address;
import com.dietergandalf.store_manager.model.Customer;
import com.dietergandalf.store_manager.model.ShoppingCart;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    public CustomerDto toDto(Customer customer) {
        if (customer == null) {
            return null;
        }

        return CustomerDto.builder()
                .personId(customer.getPerson_id())
                .firstName(customer.getFirst_name())
                .lastName(customer.getLast_name())
                .dateOfBirth(customer.getDate_of_birth())
                .phoneNumber(customer.getPhone_number())
                .address(toAddressDto(customer.getAddress()))
                .email(customer.getEmail())
                .cartId(customer.getCart() != null ? customer.getCart().getCart_id() : null)
                .build();
    }

    public List<CustomerDto> toDtoList(List<Customer> customers) {
        return customers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Customer fromRegisterRequest(RegisterRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Customer customer = new Customer();
        customer.setFirst_name(dto.getFirstName());
        customer.setLast_name(dto.getLastName());
        customer.setDate_of_birth(dto.getDateOfBirth());
        customer.setPhone_number(dto.getPhoneNumber());
        customer.setEmail(dto.getEmail());
        customer.setPassword(dto.getPassword()); // Should be hashed in service
        customer.setAddress(fromAddressDto(dto.getAddress()));
        
        // Create a new shopping cart for the customer
        ShoppingCart cart = new ShoppingCart();
        cart.setCustomer(customer);
        customer.setCart(cart);

        return customer;
    }

    public void updateFromDto(Customer customer, UpdateProfileRequestDto dto) {
        if (dto.getFirstName() != null) {
            customer.setFirst_name(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            customer.setLast_name(dto.getLastName());
        }
        if (dto.getDateOfBirth() != null) {
            customer.setDate_of_birth(dto.getDateOfBirth());
        }
        if (dto.getPhoneNumber() != null) {
            customer.setPhone_number(dto.getPhoneNumber());
        }
        if (dto.getEmail() != null) {
            customer.setEmail(dto.getEmail());
        }
        if (dto.getAddress() != null) {
            customer.setAddress(fromAddressDto(dto.getAddress()));
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
