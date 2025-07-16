package com.dietergandalf.store_manager.service;

import com.dietergandalf.store_manager.dto.AddToCartRequestDto;
import com.dietergandalf.store_manager.dto.CustomerDto;
import com.dietergandalf.store_manager.dto.ProductStockDto;
import com.dietergandalf.store_manager.dto.RegisterRequestDto;
import com.dietergandalf.store_manager.dto.ShoppingCartDto;
import com.dietergandalf.store_manager.dto.UpdateProfileRequestDto;
import com.dietergandalf.store_manager.model.CartItem;
import com.dietergandalf.store_manager.model.Customer;
import com.dietergandalf.store_manager.model.Product;
import com.dietergandalf.store_manager.model.ProductStock;
import com.dietergandalf.store_manager.model.Seller;
import com.dietergandalf.store_manager.model.ShoppingCart;
import com.dietergandalf.store_manager.repository.CartItemRepository;
import com.dietergandalf.store_manager.repository.CustomerRepository;
import com.dietergandalf.store_manager.repository.ProductStockRepository;
import com.dietergandalf.store_manager.repository.ShoppingCartRepository;
import com.dietergandalf.store_manager.util.CartItemMapper;
import com.dietergandalf.store_manager.util.CustomerMapper;
import com.dietergandalf.store_manager.util.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    
    @Mock
    private ProductStockRepository productStockRepository;
    
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    
    @Mock
    private CartItemRepository cartItemRepository;
    
    @Mock
    private CustomerMapper customerMapper;
    
    @Mock
    private ProductMapper productMapper;
    
    @Mock
    private CartItemMapper cartItemMapper;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(
                customerRepository, 
                productStockRepository,
                shoppingCartRepository,
                cartItemRepository,
                customerMapper,
                productMapper,
                cartItemMapper
        );
    }

    @Test
    void getAllCustomers_ShouldReturnAllCustomers() {
        // Given
        Customer customer1 = createTestCustomer(1L, "John", "Doe");
        Customer customer2 = createTestCustomer(2L, "Jane", "Smith");
        List<Customer> customers = Arrays.asList(customer1, customer2);
        
        CustomerDto customerDto1 = createTestCustomerDto(1L, "John", "Doe");
        CustomerDto customerDto2 = createTestCustomerDto(2L, "Jane", "Smith");
        List<CustomerDto> customerDtos = Arrays.asList(customerDto1, customerDto2);

        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.toDtoList(customers)).thenReturn(customerDtos);

        // When
        List<CustomerDto> result = customerService.getAllCustomers();

        // Then
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(customerRepository).findAll();
        verify(customerMapper).toDtoList(customers);
    }

    @Test
    void getCustomerById_WhenCustomerExists_ShouldReturnCustomer() {
        // Given
        Long customerId = 1L;
        Customer customer = createTestCustomer(customerId, "John", "Doe");
        CustomerDto customerDto = createTestCustomerDto(customerId, "John", "Doe");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        // When
        CustomerDto result = customerService.getCustomerById(customerId);

        // Then
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(customerRepository).findById(customerId);
        verify(customerMapper).toDto(customer);
    }

    @Test
    void getCustomerById_WhenCustomerNotExists_ShouldReturnNull() {
        // Given
        Long customerId = 999L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When
        CustomerDto result = customerService.getCustomerById(customerId);

        // Then
        assertNull(result);
        verify(customerRepository).findById(customerId);
        verifyNoInteractions(customerMapper);
    }

    @Test
    void createCustomer_WithValidData_ShouldCreateCustomer() {
        // Given
        RegisterRequestDto registerRequest = RegisterRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .build();

        Customer customer = createTestCustomer(null, "John", "Doe");
        Customer savedCustomer = createTestCustomer(1L, "John", "Doe");
        CustomerDto customerDto = createTestCustomerDto(1L, "John", "Doe");

        when(customerRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(customerMapper.fromRegisterRequest(registerRequest)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(savedCustomer);
        when(customerMapper.toDto(savedCustomer)).thenReturn(customerDto);

        // When
        CustomerDto result = customerService.createCustomer(registerRequest);

        // Then
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(customerRepository).existsByEmail(registerRequest.getEmail());
        verify(customerMapper).fromRegisterRequest(registerRequest);
        verify(customerRepository).save(customer);
        verify(customerMapper).toDto(savedCustomer);
    }

    @Test
    void createCustomer_WithExistingEmail_ShouldThrowException() {
        // Given
        RegisterRequestDto registerRequest = RegisterRequestDto.builder()
                .email("existing@example.com")
                .build();

        when(customerRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> customerService.createCustomer(registerRequest));
        assertEquals("Email already exists", exception.getMessage());
        verify(customerRepository).existsByEmail(registerRequest.getEmail());
        verifyNoMoreInteractions(customerRepository, customerMapper);
    }

    @Test
    void updateCustomer_WhenCustomerExists_ShouldUpdateCustomer() {
        // Given
        Long customerId = 1L;
        UpdateProfileRequestDto updateRequest = UpdateProfileRequestDto.builder()
                .firstName("Updated John")
                .lastName("Updated Doe")
                .build();

        Customer existingCustomer = createTestCustomer(customerId, "John", "Doe");
        Customer updatedCustomer = createTestCustomer(customerId, "Updated John", "Updated Doe");
        CustomerDto customerDto = createTestCustomerDto(customerId, "Updated John", "Updated Doe");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(existingCustomer)).thenReturn(updatedCustomer);
        when(customerMapper.toDto(updatedCustomer)).thenReturn(customerDto);

        // When
        CustomerDto result = customerService.updateCustomer(customerId, updateRequest);

        // Then
        assertNotNull(result);
        assertEquals("Updated John", result.getFirstName());
        verify(customerRepository).findById(customerId);
        verify(customerMapper).updateFromDto(existingCustomer, updateRequest);
        verify(customerRepository).save(existingCustomer);
        verify(customerMapper).toDto(updatedCustomer);
    }

    @Test
    void updateCustomer_WhenCustomerNotExists_ShouldReturnNull() {
        // Given
        Long customerId = 999L;
        UpdateProfileRequestDto updateRequest = UpdateProfileRequestDto.builder().build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When
        CustomerDto result = customerService.updateCustomer(customerId, updateRequest);

        // Then
        assertNull(result);
        verify(customerRepository).findById(customerId);
        verifyNoMoreInteractions(customerRepository, customerMapper);
    }

    @Test
    void deleteCustomer_WhenCustomerExists_ShouldReturnTrue() {
        // Given
        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(true);

        // When
        boolean result = customerService.deleteCustomer(customerId);

        // Then
        assertTrue(result);
        verify(customerRepository).existsById(customerId);
        verify(customerRepository).deleteById(customerId);
    }

    @Test
    void deleteCustomer_WhenCustomerNotExists_ShouldReturnFalse() {
        // Given
        Long customerId = 999L;
        when(customerRepository.existsById(customerId)).thenReturn(false);

        // When
        boolean result = customerService.deleteCustomer(customerId);

        // Then
        assertFalse(result);
        verify(customerRepository).existsById(customerId);
        verify(customerRepository, never()).deleteById(anyLong());
    }

    @Test
    void getAllAvailableProducts_ShouldReturnAvailableProducts() {
        // Given
        ProductStock productStock1 = createTestProductStock(1L, 10);
        ProductStock productStock2 = createTestProductStock(2L, 5);
        List<ProductStock> productStocks = Arrays.asList(productStock1, productStock2);
        
        ProductStockDto productStockDto1 = createTestProductStockDto(1L, 10);
        ProductStockDto productStockDto2 = createTestProductStockDto(2L, 5);
        List<ProductStockDto> productStockDtos = Arrays.asList(productStockDto1, productStockDto2);

        when(productStockRepository.findByAmountGreaterThan(0)).thenReturn(productStocks);
        when(productMapper.toStockDtoList(productStocks)).thenReturn(productStockDtos);

        // When
        List<ProductStockDto> result = customerService.getAllAvailableProducts();

        // Then
        assertEquals(2, result.size());
        verify(productStockRepository).findByAmountGreaterThan(0);
        verify(productMapper).toStockDtoList(productStocks);
    }

    @Test
    void addToCart_WithValidData_ShouldAddItemToCart() {
        // Given
        Long customerId = 1L;
        AddToCartRequestDto addToCartRequest = AddToCartRequestDto.builder()
                .productStockId(1L)
                .quantity(2)
                .build();

        Customer customer = createTestCustomer(customerId, "John", "Doe");
        ProductStock productStock = createTestProductStock(1L, 10);
        ShoppingCart cart = createTestShoppingCart(1L, customer);
        customer.setCart(cart);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(productStockRepository.findById(addToCartRequest.getProductStockId())).thenReturn(Optional.of(productStock));
        when(cartItemRepository.findByCartAndProductStock(cart, productStock)).thenReturn(Optional.empty());
        when(cartItemRepository.findByCart(cart)).thenReturn(Collections.emptyList());

        ShoppingCartDto expectedCartDto = ShoppingCartDto.builder()
                .cartId(1L)
                .customerId(customerId)
                .build();

        // When
        ShoppingCartDto result = customerService.addToCart(customerId, addToCartRequest);

        // Then
        assertNotNull(result);
        verify(customerRepository).findById(customerId);
        verify(productStockRepository).findById(addToCartRequest.getProductStockId());
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    void addToCart_WithInsufficientStock_ShouldThrowException() {
        // Given
        Long customerId = 1L;
        AddToCartRequestDto addToCartRequest = AddToCartRequestDto.builder()
                .productStockId(1L)
                .quantity(20) // More than available stock
                .build();

        Customer customer = createTestCustomer(customerId, "John", "Doe");
        ProductStock productStock = createTestProductStock(1L, 10); // Only 10 available

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(productStockRepository.findById(addToCartRequest.getProductStockId())).thenReturn(Optional.of(productStock));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> customerService.addToCart(customerId, addToCartRequest));
        assertEquals("Insufficient stock", exception.getMessage());
    }

    @Test
    void addToCart_WithNonExistentCustomer_ShouldThrowException() {
        // Given
        Long customerId = 999L;
        AddToCartRequestDto addToCartRequest = AddToCartRequestDto.builder()
                .productStockId(1L)
                .quantity(2)
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> customerService.addToCart(customerId, addToCartRequest));
        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    void clearCart_WhenCustomerAndCartExist_ShouldReturnTrue() {
        // Given
        Long customerId = 1L;
        Customer customer = createTestCustomer(customerId, "John", "Doe");
        ShoppingCart cart = createTestShoppingCart(1L, customer);
        customer.setCart(cart);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // When
        boolean result = customerService.clearCart(customerId);

        // Then
        assertTrue(result);
        verify(customerRepository).findById(customerId);
        verify(cartItemRepository).deleteByCart(cart);
    }

    @Test
    void clearCart_WhenCustomerNotExists_ShouldReturnFalse() {
        // Given
        Long customerId = 999L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When
        boolean result = customerService.clearCart(customerId);

        // Then
        assertFalse(result);
        verify(customerRepository).findById(customerId);
        verifyNoInteractions(cartItemRepository);
    }

    // Helper methods for creating test objects
    private Customer createTestCustomer(Long id, String firstName, String lastName) {
        Customer customer = new Customer();
        customer.setPerson_id(id);
        customer.setFirst_name(firstName);
        customer.setLast_name(lastName);
        customer.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com");
        return customer;
    }

    private CustomerDto createTestCustomerDto(Long id, String firstName, String lastName) {
        return CustomerDto.builder()
                .personId(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com")
                .build();
    }

    private ProductStock createTestProductStock(Long id, Integer amount) {
        ProductStock productStock = new ProductStock();
        productStock.setProduct_stock_id(id != null ? id : 0);
        productStock.setAmount(amount);
        
        Product product = new Product();
        product.setProduct_id(id);
        product.setName("Test Product " + id);
        product.setPrice(10.0);
        productStock.setProduct(product);
        
        return productStock;
    }

    private ProductStockDto createTestProductStockDto(Long id, Integer amount) {
        return ProductStockDto.builder()
                .productStockId(id)
                .amount(amount)
                .build();
    }

    private ShoppingCart createTestShoppingCart(Long id, Customer customer) {
        ShoppingCart cart = new ShoppingCart();
        cart.setCart_id(id);
        cart.setCustomer(customer);
        return cart;
    }
}