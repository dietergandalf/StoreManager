package com.dietergandalf.store_manager.util;

import com.dietergandalf.store_manager.dto.AddressDto;
import com.dietergandalf.store_manager.dto.CustomerDto;
import com.dietergandalf.store_manager.dto.RegisterRequestDto;
import com.dietergandalf.store_manager.dto.UpdateProfileRequestDto;
import com.dietergandalf.store_manager.model.Address;
import com.dietergandalf.store_manager.model.Customer;
import com.dietergandalf.store_manager.model.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        customerMapper = new CustomerMapper();
    }

    @Test
    void toDto_WithCompleteCustomer_ShouldMapAllFields() {
        // Given
        Address address = createTestAddress();
        ShoppingCart cart = createTestShoppingCart(1L);
        Customer customer = createTestCustomer(1L, "John", "Doe", "john@example.com", address, cart);

        // When
        CustomerDto result = customerMapper.toDto(customer);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getPersonId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("1990-01-01", result.getDateOfBirth());
        assertEquals("1234567890", result.getPhoneNumber());
        assertEquals(1L, result.getCartId());
        
        assertNotNull(result.getAddress());
        assertEquals("123 Main St", result.getAddress().getStreet());
        assertEquals("Anytown", result.getAddress().getCity());
    }

    @Test
    void toDto_WithCustomerWithoutCart_ShouldMapWithNullCartId() {
        // Given
        Customer customer = createTestCustomer(1L, "Jane", "Smith", "jane@example.com", null, null);

        // When
        CustomerDto result = customerMapper.toDto(customer);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getPersonId());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertNull(result.getCartId());
    }

    @Test
    void toDto_WithNullCustomer_ShouldReturnNull() {
        // When
        CustomerDto result = customerMapper.toDto(null);

        // Then
        assertNull(result);
    }

    @Test
    void toDtoList_WithMultipleCustomers_ShouldMapAllCustomers() {
        // Given
        Customer customer1 = createTestCustomer(1L, "John", "Doe", "john@example.com", null, null);
        Customer customer2 = createTestCustomer(2L, "Jane", "Smith", "jane@example.com", null, null);
        List<Customer> customers = Arrays.asList(customer1, customer2);

        // When
        List<CustomerDto> result = customerMapper.toDtoList(customers);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals("Smith", result.get(1).getLastName());
    }

    @Test
    void toDtoList_WithEmptyList_ShouldReturnEmptyList() {
        // Given
        List<Customer> emptyList = Collections.emptyList();

        // When
        List<CustomerDto> result = customerMapper.toDtoList(emptyList);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void fromRegisterRequest_WithCompleteRequest_ShouldMapAllFields() {
        // Given
        AddressDto addressDto = AddressDto.builder()
                .street("456 Oak Ave")
                .city("Springfield")
                .province("IL")
                .postalCode("62701")
                .country("USA")
                .build();

        RegisterRequestDto registerRequest = RegisterRequestDto.builder()
                .firstName("Alice")
                .lastName("Brown")
                .email("alice@example.com")
                .password("password123")
                .phoneNumber("5555551234")
                .dateOfBirth("1985-05-15")
                .address(addressDto)
                .build();

        // When
        Customer result = customerMapper.fromRegisterRequest(registerRequest);

        // Then
        assertNotNull(result);
        assertEquals("Alice", result.getFirst_name());
        assertEquals("Brown", result.getLast_name());
        assertEquals("alice@example.com", result.getEmail());
        assertEquals("password123", result.getPassword());
        assertEquals("5555551234", result.getPhone_number());
        assertEquals("1985-05-15", result.getDate_of_birth());
        
        assertNotNull(result.getAddress());
        assertEquals("456 Oak Ave", result.getAddress().getStreet());
        assertEquals("Springfield", result.getAddress().getCity());
    }

    @Test
    void fromRegisterRequest_WithoutAddress_ShouldMapWithNullAddress() {
        // Given
        RegisterRequestDto registerRequest = RegisterRequestDto.builder()
                .firstName("Bob")
                .lastName("Wilson")
                .email("bob@example.com")
                .password("password456")
                .phoneNumber("5555559876")
                .dateOfBirth("1992-03-20")
                .build();

        // When
        Customer result = customerMapper.fromRegisterRequest(registerRequest);

        // Then
        assertNotNull(result);
        assertEquals("Bob", result.getFirst_name());
        assertEquals("Wilson", result.getLast_name());
        assertEquals("bob@example.com", result.getEmail());
        assertNull(result.getAddress());
    }

    @Test
    void fromRegisterRequest_WithNullRequest_ShouldReturnNull() {
        // When
        Customer result = customerMapper.fromRegisterRequest(null);

        // Then
        assertNull(result);
    }

    @Test
    void updateFromDto_WithCompleteUpdateRequest_ShouldUpdateAllFields() {
        // Given
        Customer existingCustomer = createTestCustomer(1L, "Old", "Name", "old@example.com", null, null);
        
        AddressDto newAddressDto = AddressDto.builder()
                .street("789 New St")
                .city("New City")
                .province("NY")
                .postalCode("10001")
                .country("USA")
                .build();

        UpdateProfileRequestDto updateRequest = UpdateProfileRequestDto.builder()
                .firstName("Updated")
                .lastName("Name")
                .email("updated@example.com")
                .phoneNumber("9999999999")
                .dateOfBirth("1988-12-25")
                .address(newAddressDto)
                .build();

        // When
        customerMapper.updateFromDto(existingCustomer, updateRequest);

        // Then
        assertEquals("Updated", existingCustomer.getFirst_name());
        assertEquals("Name", existingCustomer.getLast_name());
        assertEquals("updated@example.com", existingCustomer.getEmail());
        assertEquals("9999999999", existingCustomer.getPhone_number());
        assertEquals("1988-12-25", existingCustomer.getDate_of_birth());
        
        assertNotNull(existingCustomer.getAddress());
        assertEquals("789 New St", existingCustomer.getAddress().getStreet());
        assertEquals("New City", existingCustomer.getAddress().getCity());
    }

    @Test
    void updateFromDto_WithPartialUpdateRequest_ShouldUpdateOnlyProvidedFields() {
        // Given
        Customer existingCustomer = createTestCustomer(1L, "John", "Doe", "john@example.com", null, null);
        existingCustomer.setPhone_number("1111111111");

        UpdateProfileRequestDto updateRequest = UpdateProfileRequestDto.builder()
                .firstName("UpdatedJohn")
                .email("updated.john@example.com")
                .build();

        // When
        customerMapper.updateFromDto(existingCustomer, updateRequest);

        // Then
        assertEquals("UpdatedJohn", existingCustomer.getFirst_name());
        assertEquals("Doe", existingCustomer.getLast_name()); // Should remain unchanged
        assertEquals("updated.john@example.com", existingCustomer.getEmail());
        assertEquals("1111111111", existingCustomer.getPhone_number()); // Should remain unchanged
    }

    // Helper methods for creating test objects
    private Customer createTestCustomer(Long id, String firstName, String lastName, String email, Address address, ShoppingCart cart) {
        Customer customer = new Customer();
        customer.setPerson_id(id);
        customer.setFirst_name(firstName);
        customer.setLast_name(lastName);
        customer.setEmail(email);
        customer.setPassword("password123");
        customer.setPhone_number("1234567890");
        customer.setDate_of_birth("1990-01-01");
        customer.setAddress(address);
        customer.setCart(cart);
        return customer;
    }

    private Address createTestAddress() {
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("Anytown");
        address.setProvince("ST");
        address.setPostalCode("12345");
        address.setCountry("USA");
        return address;
    }

    private ShoppingCart createTestShoppingCart(Long id) {
        ShoppingCart cart = new ShoppingCart();
        cart.setCart_id(id);
        return cart;
    }
}
