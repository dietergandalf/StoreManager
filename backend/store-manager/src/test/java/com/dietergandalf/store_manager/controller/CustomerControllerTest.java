package com.dietergandalf.store_manager.controller;

import com.dietergandalf.store_manager.dto.AddToCartRequestDto;
import com.dietergandalf.store_manager.dto.CustomerDto;
import com.dietergandalf.store_manager.dto.ProductStockDto;
import com.dietergandalf.store_manager.dto.RegisterRequestDto;
import com.dietergandalf.store_manager.dto.ShoppingCartDto;
import com.dietergandalf.store_manager.dto.UpdateProfileRequestDto;
import com.dietergandalf.store_manager.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerDto testCustomerDto;
    private RegisterRequestDto testRegisterRequest;
    private UpdateProfileRequestDto testUpdateRequest;

    @BeforeEach
    void setUp() {
        testCustomerDto = CustomerDto.builder()
                .personId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .build();

        testRegisterRequest = RegisterRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .phoneNumber("1234567890")
                .dateOfBirth("1990-01-01")
                .build();

        testUpdateRequest = UpdateProfileRequestDto.builder()
                .firstName("Updated John")
                .lastName("Updated Doe")
                .email("updated.john@example.com")
                .phoneNumber("0987654321")
                .build();
    }

    @Test
    @WithMockUser
    void getAllCustomers_ShouldReturnListOfCustomers() throws Exception {
        // Given
        CustomerDto customer1 = CustomerDto.builder()
                .personId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();
        CustomerDto customer2 = CustomerDto.builder()
                .personId(2L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane@example.com")
                .build();
        List<CustomerDto> customers = Arrays.asList(customer1, customer2);

        when(customerService.getAllCustomers()).thenReturn(customers);

        // When & Then
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"));

        verify(customerService).getAllCustomers();
    }

    @Test
    @WithMockUser
    void getAllCustomers_WhenNoCustomers_ShouldReturnEmptyList() throws Exception {
        // Given
        when(customerService.getAllCustomers()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(customerService).getAllCustomers();
    }

    @Test
    @WithMockUser
    void createCustomer_WithValidData_ShouldReturnCreatedCustomer() throws Exception {
        // Given
        when(customerService.createCustomer(any(RegisterRequestDto.class))).thenReturn(testCustomerDto);

        // When & Then
        mockMvc.perform(post("/api/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRegisterRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.personId").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(customerService).createCustomer(any(RegisterRequestDto.class));
    }

    @Test
    @WithMockUser
    void createCustomer_WithDuplicateEmail_ShouldReturnBadRequest() throws Exception {
        // Given
        when(customerService.createCustomer(any(RegisterRequestDto.class)))
                .thenThrow(new RuntimeException("Email already exists"));

        // When & Then
        mockMvc.perform(post("/api/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRegisterRequest)))
                .andExpect(status().isBadRequest());

        verify(customerService).createCustomer(any(RegisterRequestDto.class));
    }

    @Test
    @WithMockUser
    void getCustomerById_WhenCustomerExists_ShouldReturnCustomer() throws Exception {
        // Given
        Long customerId = 1L;
        when(customerService.getCustomerById(customerId)).thenReturn(testCustomerDto);

        // When & Then
        mockMvc.perform(get("/api/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.personId").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(customerService).getCustomerById(customerId);
    }

    @Test
    @WithMockUser
    void getCustomerById_WhenCustomerNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        Long customerId = 999L;
        when(customerService.getCustomerById(customerId)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/customers/{id}", customerId))
                .andExpect(status().isNotFound());

        verify(customerService).getCustomerById(customerId);
    }

    @Test
    @WithMockUser
    void updateCustomer_WhenCustomerExists_ShouldReturnUpdatedCustomer() throws Exception {
        // Given
        Long customerId = 1L;
        CustomerDto updatedCustomer = CustomerDto.builder()
                .personId(customerId)
                .firstName("Updated John")
                .lastName("Updated Doe")
                .email("updated.john@example.com")
                .build();

        when(customerService.updateCustomer(eq(customerId), any(UpdateProfileRequestDto.class)))
                .thenReturn(updatedCustomer);

        // When & Then
        mockMvc.perform(put("/api/customers/{id}", customerId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Updated John"))
                .andExpect(jsonPath("$.lastName").value("Updated Doe"));

        verify(customerService).updateCustomer(eq(customerId), any(UpdateProfileRequestDto.class));
    }

    @Test
    @WithMockUser
    void updateCustomer_WhenCustomerNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        Long customerId = 999L;
        when(customerService.updateCustomer(eq(customerId), any(UpdateProfileRequestDto.class)))
                .thenReturn(null);

        // When & Then
        mockMvc.perform(put("/api/customers/{id}", customerId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUpdateRequest)))
                .andExpect(status().isNotFound());

        verify(customerService).updateCustomer(eq(customerId), any(UpdateProfileRequestDto.class));
    }

    @Test
    @WithMockUser
    void deleteCustomer_WhenCustomerExists_ShouldReturnNoContent() throws Exception {
        // Given
        Long customerId = 1L;
        when(customerService.deleteCustomer(customerId)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/customers/{id}", customerId)
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer(customerId);
    }

    @Test
    @WithMockUser
    void deleteCustomer_WhenCustomerNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        Long customerId = 999L;
        when(customerService.deleteCustomer(customerId)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/customers/{id}", customerId)
                .with(csrf()))
                .andExpect(status().isNotFound());

        verify(customerService).deleteCustomer(customerId);
    }

    @Test
    @WithMockUser
    void getAllAvailableProducts_ShouldReturnProductList() throws Exception {
        // Given
        ProductStockDto product1 = ProductStockDto.builder()
                .productStockId(1L)
                .amount(50)
                .build();
        ProductStockDto product2 = ProductStockDto.builder()
                .productStockId(2L)
                .amount(30)
                .build();
        List<ProductStockDto> products = Arrays.asList(product1, product2);

        when(customerService.getAllAvailableProducts()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/customers/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].amount").value(50))
                .andExpect(jsonPath("$[1].amount").value(30));

        verify(customerService).getAllAvailableProducts();
    }

    @Test
    @WithMockUser
    void addToCart_WithValidData_ShouldReturnShoppingCart() throws Exception {
        // Given
        Long customerId = 1L;
        AddToCartRequestDto addToCartRequest = AddToCartRequestDto.builder()
                .productStockId(1L)
                .quantity(2)
                .build();

        ShoppingCartDto cartDto = ShoppingCartDto.builder()
                .cartId(1L)
                .customerId(customerId)
                .customerName("John Doe")
                .totalAmount(20.0)
                .totalItems(2)
                .build();

        when(customerService.addToCart(eq(customerId), any(AddToCartRequestDto.class)))
                .thenReturn(cartDto);

        // When & Then
        mockMvc.perform(post("/api/customers/{customerId}/cart", customerId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addToCartRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cartId").value(1L))
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.totalAmount").value(20.0))
                .andExpect(jsonPath("$.totalItems").value(2));

        verify(customerService).addToCart(eq(customerId), any(AddToCartRequestDto.class));
    }

    @Test
    @WithMockUser
    void addToCart_WithInsufficientStock_ShouldReturnBadRequest() throws Exception {
        // Given
        Long customerId = 1L;
        AddToCartRequestDto addToCartRequest = AddToCartRequestDto.builder()
                .productStockId(1L)
                .quantity(100)
                .build();

        when(customerService.addToCart(eq(customerId), any(AddToCartRequestDto.class)))
                .thenThrow(new RuntimeException("Insufficient stock"));

        // When & Then
        mockMvc.perform(post("/api/customers/{customerId}/cart", customerId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addToCartRequest)))
                .andExpect(status().isBadRequest());

        verify(customerService).addToCart(eq(customerId), any(AddToCartRequestDto.class));
    }

    @Test
    @WithMockUser
    void getCart_WhenCartExists_ShouldReturnShoppingCart() throws Exception {
        // Given
        Long customerId = 1L;
        ShoppingCartDto cartDto = ShoppingCartDto.builder()
                .cartId(1L)
                .customerId(customerId)
                .customerName("John Doe")
                .totalAmount(30.0)
                .totalItems(3)
                .build();

        when(customerService.getCart(customerId)).thenReturn(cartDto);

        // When & Then
        mockMvc.perform(get("/api/customers/{customerId}/cart", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cartId").value(1L))
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.totalAmount").value(30.0));

        verify(customerService).getCart(customerId);
    }

    @Test
    @WithMockUser
    void getCart_WhenCartNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        Long customerId = 999L;
        when(customerService.getCart(customerId)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/customers/{customerId}/cart", customerId))
                .andExpect(status().isNotFound());

        verify(customerService).getCart(customerId);
    }

    @Test
    @WithMockUser
    void clearCart_WhenCartExists_ShouldReturnNoContent() throws Exception {
        // Given
        Long customerId = 1L;
        when(customerService.clearCart(customerId)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/customers/{customerId}/cart", customerId)
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(customerService).clearCart(customerId);
    }

    @Test
    @WithMockUser
    void clearCart_WhenCartNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        Long customerId = 999L;
        when(customerService.clearCart(customerId)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/customers/{customerId}/cart", customerId)
                .with(csrf()))
                .andExpect(status().isNotFound());

        verify(customerService).clearCart(customerId);
    }
}
