package com.dietergandalf.store_manager.service;

import com.dietergandalf.store_manager.dto.CreateProductRequestDto;
import com.dietergandalf.store_manager.dto.ProductDto;
import com.dietergandalf.store_manager.dto.ProductStockDto;
import com.dietergandalf.store_manager.dto.RegisterRequestDto;
import com.dietergandalf.store_manager.dto.SellerDto;
import com.dietergandalf.store_manager.dto.UpdateProfileRequestDto;
import com.dietergandalf.store_manager.model.Product;
import com.dietergandalf.store_manager.model.ProductStock;
import com.dietergandalf.store_manager.model.Seller;
import com.dietergandalf.store_manager.repository.ProductRepository;
import com.dietergandalf.store_manager.repository.ProductStockRepository;
import com.dietergandalf.store_manager.repository.SellerRepository;
import com.dietergandalf.store_manager.util.ProductMapper;
import com.dietergandalf.store_manager.util.SellerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private ProductStockRepository productStockRepository;
    
    @Mock
    private SellerMapper sellerMapper;
    
    @Mock
    private ProductMapper productMapper;

    private SellerService sellerService;

    @BeforeEach
    void setUp() {
        sellerService = new SellerService(
                sellerRepository,
                productRepository,
                productStockRepository,
                sellerMapper,
                productMapper
        );
    }

    @Test
    void getAllSellers_ShouldReturnAllSellers() {
        // Given
        Seller seller1 = createTestSeller(1L, "John", "Doe");
        Seller seller2 = createTestSeller(2L, "Jane", "Smith");
        List<Seller> sellers = Arrays.asList(seller1, seller2);
        
        SellerDto sellerDto1 = createTestSellerDto(1L, "John", "Doe");
        SellerDto sellerDto2 = createTestSellerDto(2L, "Jane", "Smith");
        List<SellerDto> sellerDtos = Arrays.asList(sellerDto1, sellerDto2);

        when(sellerRepository.findAll()).thenReturn(sellers);
        when(sellerMapper.toDtoList(sellers)).thenReturn(sellerDtos);

        // When
        List<SellerDto> result = sellerService.getAllSellers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        
        verify(sellerRepository).findAll();
        verify(sellerMapper).toDtoList(sellers);
    }

    @Test
    void getSellerById_WhenSellerExists_ShouldReturnSeller() {
        // Given
        Long sellerId = 1L;
        Seller seller = createTestSeller(sellerId, "John", "Doe");
        SellerDto sellerDto = createTestSellerDto(sellerId, "John", "Doe");

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(seller));
        when(sellerMapper.toDto(seller)).thenReturn(sellerDto);

        // When
        SellerDto result = sellerService.getSellerById(sellerId);

        // Then
        assertNotNull(result);
        assertEquals(sellerId, result.getPersonId());
        assertEquals("John", result.getFirstName());
        
        verify(sellerRepository).findById(sellerId);
        verify(sellerMapper).toDto(seller);
    }

    @Test
    void getSellerById_WhenSellerNotExists_ShouldReturnNull() {
        // Given
        Long sellerId = 999L;
        when(sellerRepository.findById(sellerId)).thenReturn(Optional.empty());

        // When
        SellerDto result = sellerService.getSellerById(sellerId);

        // Then
        assertNull(result);
        verify(sellerRepository).findById(sellerId);
        verifyNoInteractions(sellerMapper);
    }

    @Test
    void createSeller_WithValidData_ShouldCreateSeller() {
        // Given
        RegisterRequestDto registerRequest = RegisterRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .phoneNumber("1234567890")
                .build();

        Seller seller = createTestSeller(null, "John", "Doe");
        Seller savedSeller = createTestSeller(1L, "John", "Doe");
        SellerDto sellerDto = createTestSellerDto(1L, "John", "Doe");

        when(sellerRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(sellerMapper.fromRegisterRequest(registerRequest)).thenReturn(seller);
        when(sellerRepository.save(seller)).thenReturn(savedSeller);
        when(sellerMapper.toDto(savedSeller)).thenReturn(sellerDto);

        // When
        SellerDto result = sellerService.createSeller(registerRequest);

        // Then
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        
        verify(sellerRepository).existsByEmail(registerRequest.getEmail());
        verify(sellerMapper).fromRegisterRequest(registerRequest);
        verify(sellerRepository).save(seller);
        verify(sellerMapper).toDto(savedSeller);
    }

    @Test
    void createSeller_WithExistingEmail_ShouldThrowException() {
        // Given
        RegisterRequestDto registerRequest = RegisterRequestDto.builder()
                .email("existing@example.com")
                .build();

        when(sellerRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> sellerService.createSeller(registerRequest));
        assertEquals("Email already exists", exception.getMessage());
        
        verify(sellerRepository).existsByEmail(registerRequest.getEmail());
        verifyNoMoreInteractions(sellerRepository, sellerMapper);
    }

    @Test
    void updateSeller_WhenSellerExists_ShouldUpdateSeller() {
        // Given
        Long sellerId = 1L;
        UpdateProfileRequestDto updateRequest = UpdateProfileRequestDto.builder()
                .firstName("Updated John")
                .lastName("Updated Doe")
                .build();

        Seller existingSeller = createTestSeller(sellerId, "John", "Doe");
        Seller updatedSeller = createTestSeller(sellerId, "Updated John", "Updated Doe");
        SellerDto sellerDto = createTestSellerDto(sellerId, "Updated John", "Updated Doe");

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(existingSeller));
        when(sellerRepository.save(existingSeller)).thenReturn(updatedSeller);
        when(sellerMapper.toDto(updatedSeller)).thenReturn(sellerDto);

        // When
        SellerDto result = sellerService.updateSeller(sellerId, updateRequest);

        // Then
        assertNotNull(result);
        assertEquals("Updated John", result.getFirstName());
        
        verify(sellerRepository).findById(sellerId);
        verify(sellerMapper).updateFromDto(existingSeller, updateRequest);
        verify(sellerRepository).save(existingSeller);
        verify(sellerMapper).toDto(updatedSeller);
    }

    @Test
    void updateSeller_WhenSellerNotExists_ShouldReturnNull() {
        // Given
        Long sellerId = 999L;
        UpdateProfileRequestDto updateRequest = UpdateProfileRequestDto.builder().build();

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.empty());

        // When
        SellerDto result = sellerService.updateSeller(sellerId, updateRequest);

        // Then
        assertNull(result);
        verify(sellerRepository).findById(sellerId);
        verifyNoMoreInteractions(sellerRepository, sellerMapper);
    }

    @Test
    void deleteSeller_WhenSellerExists_ShouldReturnTrue() {
        // Given
        Long sellerId = 1L;
        when(sellerRepository.existsById(sellerId)).thenReturn(true);

        // When
        boolean result = sellerService.deleteSeller(sellerId);

        // Then
        assertTrue(result);
        verify(sellerRepository).existsById(sellerId);
        verify(sellerRepository).deleteById(sellerId);
    }

    @Test
    void deleteSeller_WhenSellerNotExists_ShouldReturnFalse() {
        // Given
        Long sellerId = 999L;
        when(sellerRepository.existsById(sellerId)).thenReturn(false);

        // When
        boolean result = sellerService.deleteSeller(sellerId);

        // Then
        assertFalse(result);
        verify(sellerRepository).existsById(sellerId);
        verify(sellerRepository, never()).deleteById(anyLong());
    }

    @Test
    void addProduct_WithValidData_ShouldAddProduct() {
        // Given
        Long sellerId = 1L;
        CreateProductRequestDto productRequest = CreateProductRequestDto.builder()
                .name("Test Product")
                .description("Test Description")
                .price(10.0)
                .initialStock(50)
                .build();

        Seller seller = createTestSeller(sellerId, "John", "Doe");
        Product product = createTestProduct(null, "Test Product", 10.0);
        Product savedProduct = createTestProduct(1L, "Test Product", 10.0);
        ProductStock productStock = createTestProductStock(null, savedProduct, seller, 50);
        ProductStock savedProductStock = createTestProductStock(1L, savedProduct, seller, 50);
        ProductStockDto productStockDto = createTestProductStockDto(1L, "Test Product", 10.0, 50);

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(seller));
        when(productMapper.fromCreateRequest(productRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.createProductStock(savedProduct, seller, productRequest.getInitialStock())).thenReturn(productStock);
        when(productStockRepository.save(productStock)).thenReturn(savedProductStock);
        when(productMapper.toStockDto(savedProductStock)).thenReturn(productStockDto);

        // When
        ProductStockDto result = sellerService.addProduct(sellerId, productRequest);

        // Then
        assertNotNull(result);
        assertEquals("Test Product", result.getProduct().getName());
        assertEquals(10.0, result.getProduct().getPrice());
        assertEquals(50, result.getAmount());
        
        verify(sellerRepository).findById(sellerId);
        verify(productMapper).fromCreateRequest(productRequest);
        verify(productRepository).save(product);
        verify(productMapper).createProductStock(savedProduct, seller, productRequest.getInitialStock());
        verify(productStockRepository).save(productStock);
        verify(productMapper).toStockDto(savedProductStock);
    }

    @Test
    void addProduct_WithNonExistentSeller_ShouldThrowException() {
        // Given
        Long sellerId = 999L;
        CreateProductRequestDto productRequest = CreateProductRequestDto.builder()
                .name("Test Product")
                .build();

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> sellerService.addProduct(sellerId, productRequest));
        assertEquals("Seller not found", exception.getMessage());
        
        verify(sellerRepository).findById(sellerId);
        verifyNoMoreInteractions(productRepository, productStockRepository, productMapper);
    }

    @Test
    void getSellerProducts_ShouldReturnSellerProducts() {
        // Given
        Long sellerId = 1L;
        ProductStock productStock1 = createTestProductStock(1L, createTestProduct(1L, "Product 1", 10.0), createTestSeller(sellerId, "John", "Doe"), 20);
        ProductStock productStock2 = createTestProductStock(2L, createTestProduct(2L, "Product 2", 15.0), createTestSeller(sellerId, "John", "Doe"), 30);
        List<ProductStock> productStocks = Arrays.asList(productStock1, productStock2);
        
        ProductStockDto productStockDto1 = createTestProductStockDto(1L, "Product 1", 10.0, 20);
        ProductStockDto productStockDto2 = createTestProductStockDto(2L, "Product 2", 15.0, 30);
        List<ProductStockDto> productStockDtos = Arrays.asList(productStockDto1, productStockDto2);

        when(productStockRepository.findBySellerPersonId(sellerId)).thenReturn(productStocks);
        when(productMapper.toStockDtoList(productStocks)).thenReturn(productStockDtos);

        // When
        List<ProductStockDto> result = sellerService.getSellerProducts(sellerId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getProduct() != null && "Product 1".equals(p.getProduct().getName())));
        assertTrue(result.stream().anyMatch(p -> p.getProduct() != null && "Product 2".equals(p.getProduct().getName())));
        
        verify(productStockRepository).findBySellerPersonId(sellerId);
        verify(productMapper).toStockDtoList(productStocks);
    }

    @Test
    void updateProductStock_WithValidData_ShouldUpdateStock() {
        // Given
        Long sellerId = 1L;
        Long productStockId = 1L;
        Integer newQuantity = 100;

        Seller seller = createTestSeller(sellerId, "John", "Doe");
        ProductStock productStock = createTestProductStock(productStockId, createTestProduct(1L, "Test Product", 10.0), seller, 50);
        ProductStock updatedProductStock = createTestProductStock(productStockId, createTestProduct(1L, "Test Product", 10.0), seller, newQuantity);
        ProductStockDto productStockDto = createTestProductStockDto(productStockId, "Test Product", 10.0, newQuantity);

        when(productStockRepository.findById(productStockId)).thenReturn(Optional.of(productStock));
        when(productStockRepository.save(productStock)).thenReturn(updatedProductStock);
        when(productMapper.toStockDto(updatedProductStock)).thenReturn(productStockDto);

        // When
        ProductStockDto result = sellerService.updateProductStock(sellerId, productStockId, newQuantity);

        // Then
        assertNotNull(result);
        assertEquals(newQuantity, result.getAmount());
        
        verify(productStockRepository).findById(productStockId);
        verify(productStockRepository).save(productStock);
        verify(productMapper).toStockDto(updatedProductStock);
    }

    @Test
    void updateProductStock_WithWrongSeller_ShouldThrowException() {
        // Given
        Long sellerId = 1L;
        Long wrongSellerId = 2L;
        Long productStockId = 1L;
        Integer newQuantity = 100;

        Seller wrongSeller = createTestSeller(wrongSellerId, "Jane", "Smith");
        ProductStock productStock = createTestProductStock(productStockId, createTestProduct(1L, "Test Product", 10.0), wrongSeller, 50);

        when(productStockRepository.findById(productStockId)).thenReturn(Optional.of(productStock));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> sellerService.updateProductStock(sellerId, productStockId, newQuantity));
        assertEquals("Product does not belong to this seller", exception.getMessage());
        
        verify(productStockRepository).findById(productStockId);
        verify(productStockRepository, never()).save(any());
    }

    @Test
    void removeProduct_WithValidData_ShouldReturnTrue() {
        // Given
        Long sellerId = 1L;
        Long productStockId = 1L;

        Seller seller = createTestSeller(sellerId, "John", "Doe");
        ProductStock productStock = createTestProductStock(productStockId, createTestProduct(1L, "Test Product", 10.0), seller, 50);

        when(productStockRepository.findById(productStockId)).thenReturn(Optional.of(productStock));

        // When
        boolean result = sellerService.removeProduct(sellerId, productStockId);

        // Then
        assertTrue(result);
        verify(productStockRepository).findById(productStockId);
        verify(productStockRepository).delete(productStock);
    }

    @Test
    void removeProduct_WithNonExistentProduct_ShouldReturnFalse() {
        // Given
        Long sellerId = 1L;
        Long productStockId = 999L;

        when(productStockRepository.findById(productStockId)).thenReturn(Optional.empty());

        // When
        boolean result = sellerService.removeProduct(sellerId, productStockId);

        // Then
        assertFalse(result);
        verify(productStockRepository).findById(productStockId);
        verify(productStockRepository, never()).delete(any());
    }

    @Test
    void updateProductPrice_WithValidData_ShouldUpdatePrice() {
        // Given
        Long sellerId = 1L;
        Long productStockId = 1L;
        Double newPrice = 20.0;

        Seller seller = createTestSeller(sellerId, "John", "Doe");
        Product product = createTestProduct(1L, "Test Product", 10.0);
        ProductStock productStock = createTestProductStock(productStockId, product, seller, 50);
        ProductStockDto productStockDto = createTestProductStockDto(productStockId, "Test Product", newPrice, 50);

        when(productStockRepository.findById(productStockId)).thenReturn(Optional.of(productStock));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toStockDto(productStock)).thenReturn(productStockDto);

        // When
        ProductStockDto result = sellerService.updateProductPrice(sellerId, productStockId, newPrice);

        // Then
        assertNotNull(result);
        assertEquals(newPrice, result.getProduct().getPrice());
        
        verify(productStockRepository).findById(productStockId);
        verify(productRepository).save(product);
        verify(productMapper).toStockDto(productStock);
    }

    // Helper methods for creating test objects
    private Seller createTestSeller(Long id, String firstName, String lastName) {
        Seller seller = new Seller();
        seller.setPerson_id(id);
        seller.setFirst_name(firstName);
        seller.setLast_name(lastName);
        seller.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com");
        return seller;
    }

    private SellerDto createTestSellerDto(Long id, String firstName, String lastName) {
        return SellerDto.builder()
                .personId(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com")
                .build();
    }

    private Product createTestProduct(Long id, String name, Double price) {
        Product product = new Product();
        if (id != null) {
            product.setProduct_id(id);
        }
        product.setName(name);
        product.setPrice(price);
        product.setDescription("Test description for " + name);
        return product;
    }

    private ProductStock createTestProductStock(Long id, Product product, Seller seller, Integer amount) {
        ProductStock productStock = new ProductStock();
        productStock.setProduct_stock_id(id != null ? id : 0);
        productStock.setProduct(product);
        productStock.setSeller(seller);
        productStock.setAmount(amount);
        return productStock;
    }

    private ProductStockDto createTestProductStockDto(Long id, String productName, Double price, Integer amount) {
        ProductDto productDto = ProductDto.builder()
                .name(productName)
                .price(price)
                .build();
        return ProductStockDto.builder()
                .productStockId(id)
                .product(productDto)
                .amount(amount)
                .build();
    }
}
