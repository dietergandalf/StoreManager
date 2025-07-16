package com.dietergandalf.store_manager.repository;

import com.dietergandalf.store_manager.model.Product;
import com.dietergandalf.store_manager.model.ProductStock;
import com.dietergandalf.store_manager.model.Seller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class ProductStockRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductStockRepository productStockRepository;

    @Test
    void save_ShouldPersistProductStock() {
        // Given
        Seller seller = createTestSeller("John", "Doe", "john@example.com");
        Product product = createTestProduct("Test Product", "Description", 10.0);
        ProductStock productStock = createTestProductStock(product, seller, 50);

        entityManager.persistAndFlush(seller);
        entityManager.persistAndFlush(product);

        // When
        ProductStock savedProductStock = productStockRepository.save(productStock);

        // Then
        assertNotNull(savedProductStock);
        assertTrue(savedProductStock.getProduct_stock_id() > 0);
        assertEquals(50, savedProductStock.getAmount());
        assertEquals(seller.getPerson_id(), savedProductStock.getSeller().getPerson_id());
        assertEquals(product.getProduct_id(), savedProductStock.getProduct().getProduct_id());
    }

    @Test
    void findById_WhenProductStockExists_ShouldReturnProductStock() {
        // Given
        Seller seller = createTestSeller("Jane", "Smith", "jane@example.com");
        Product product = createTestProduct("Another Product", "Another Description", 15.0);
        ProductStock productStock = createTestProductStock(product, seller, 30);

        entityManager.persistAndFlush(seller);
        entityManager.persistAndFlush(product);
        ProductStock savedProductStock = entityManager.persistAndFlush(productStock);

        // When
        Optional<ProductStock> found = productStockRepository.findById(savedProductStock.getProduct_stock_id());

        // Then
        assertTrue(found.isPresent());
        assertEquals(savedProductStock.getProduct_stock_id(), found.get().getProduct_stock_id());
        assertEquals(30, found.get().getAmount());
    }

    @Test
    void findById_WhenProductStockNotExists_ShouldReturnEmpty() {
        // When
        Optional<ProductStock> found = productStockRepository.findById(999L);

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void findBySeller_ShouldReturnProductStocksForSeller() {
        // Given
        Seller seller1 = createTestSeller("Alice", "Brown", "alice@example.com");
        Seller seller2 = createTestSeller("Bob", "Wilson", "bob@example.com");
        
        Product product1 = createTestProduct("Product 1", "Description 1", 10.0);
        Product product2 = createTestProduct("Product 2", "Description 2", 20.0);
        Product product3 = createTestProduct("Product 3", "Description 3", 30.0);

        ProductStock stock1 = createTestProductStock(product1, seller1, 10);
        ProductStock stock2 = createTestProductStock(product2, seller1, 20);
        ProductStock stock3 = createTestProductStock(product3, seller2, 30);

        entityManager.persistAndFlush(seller1);
        entityManager.persistAndFlush(seller2);
        entityManager.persistAndFlush(product1);
        entityManager.persistAndFlush(product2);
        entityManager.persistAndFlush(product3);
        entityManager.persistAndFlush(stock1);
        entityManager.persistAndFlush(stock2);
        entityManager.persistAndFlush(stock3);

        // When
        List<ProductStock> seller1Stocks = productStockRepository.findBySeller(seller1);
        List<ProductStock> seller2Stocks = productStockRepository.findBySeller(seller2);

        // Then
        assertEquals(2, seller1Stocks.size());
        assertEquals(1, seller2Stocks.size());
        
        assertTrue(seller1Stocks.stream().allMatch(stock -> 
            stock.getSeller().getPerson_id().equals(seller1.getPerson_id())));
        assertTrue(seller2Stocks.stream().allMatch(stock -> 
            stock.getSeller().getPerson_id().equals(seller2.getPerson_id())));
    }

    @Test
    void findBySellerPersonId_ShouldReturnProductStocksForSellerId() {
        // Given
        Seller seller = createTestSeller("Charlie", "Davis", "charlie@example.com");
        Product product1 = createTestProduct("Product A", "Description A", 5.0);
        Product product2 = createTestProduct("Product B", "Description B", 8.0);

        ProductStock stock1 = createTestProductStock(product1, seller, 15);
        ProductStock stock2 = createTestProductStock(product2, seller, 25);

        entityManager.persistAndFlush(seller);
        entityManager.persistAndFlush(product1);
        entityManager.persistAndFlush(product2);
        entityManager.persistAndFlush(stock1);
        entityManager.persistAndFlush(stock2);

        // When
        List<ProductStock> stocks = productStockRepository.findBySellerPersonId(seller.getPerson_id());

        // Then
        assertEquals(2, stocks.size());
        assertTrue(stocks.stream().allMatch(stock -> 
            stock.getSeller().getPerson_id().equals(seller.getPerson_id())));
    }

    @Test
    void findByAmountGreaterThan_ShouldReturnProductStocksWithSufficientStock() {
        // Given
        Seller seller = createTestSeller("Eve", "Johnson", "eve@example.com");
        Product product1 = createTestProduct("Low Stock Product", "Description", 10.0);
        Product product2 = createTestProduct("Medium Stock Product", "Description", 15.0);
        Product product3 = createTestProduct("High Stock Product", "Description", 20.0);
        Product product4 = createTestProduct("Out of Stock Product", "Description", 25.0);

        ProductStock stock1 = createTestProductStock(product1, seller, 5);   // Below threshold
        ProductStock stock2 = createTestProductStock(product2, seller, 10);  // Equal to threshold
        ProductStock stock3 = createTestProductStock(product3, seller, 15);  // Above threshold
        ProductStock stock4 = createTestProductStock(product4, seller, 0);   // Out of stock

        entityManager.persistAndFlush(seller);
        entityManager.persistAndFlush(product1);
        entityManager.persistAndFlush(product2);
        entityManager.persistAndFlush(product3);
        entityManager.persistAndFlush(product4);
        entityManager.persistAndFlush(stock1);
        entityManager.persistAndFlush(stock2);
        entityManager.persistAndFlush(stock3);
        entityManager.persistAndFlush(stock4);

        // When
        List<ProductStock> availableStocks = productStockRepository.findByAmountGreaterThan(0);
        List<ProductStock> stocksAbove10 = productStockRepository.findByAmountGreaterThan(10);

        // Then
        assertEquals(3, availableStocks.size()); // stock1, stock2, stock3 (amount > 0)
        assertEquals(1, stocksAbove10.size());   // only stock3 (amount > 10)
        
        assertTrue(availableStocks.stream().allMatch(stock -> stock.getAmount() > 0));
        assertTrue(stocksAbove10.stream().allMatch(stock -> stock.getAmount() > 10));
    }

    @Test
    void deleteById_ShouldRemoveProductStock() {
        // Given
        Seller seller = createTestSeller("Frank", "Miller", "frank@example.com");
        Product product = createTestProduct("Delete Test Product", "Description", 12.0);
        ProductStock productStock = createTestProductStock(product, seller, 40);

        entityManager.persistAndFlush(seller);
        entityManager.persistAndFlush(product);
        ProductStock savedProductStock = entityManager.persistAndFlush(productStock);
        Long stockId = savedProductStock.getProduct_stock_id();

        // When
        productStockRepository.deleteById(stockId);
        entityManager.flush();

        // Then
        Optional<ProductStock> found = productStockRepository.findById(stockId);
        assertFalse(found.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllProductStocks() {
        // Given
        Seller seller1 = createTestSeller("Grace", "Taylor", "grace@example.com");
        Seller seller2 = createTestSeller("Henry", "Anderson", "henry@example.com");
        
        Product product1 = createTestProduct("Product X", "Description X", 5.0);
        Product product2 = createTestProduct("Product Y", "Description Y", 7.0);

        ProductStock stock1 = createTestProductStock(product1, seller1, 20);
        ProductStock stock2 = createTestProductStock(product2, seller2, 30);

        entityManager.persistAndFlush(seller1);
        entityManager.persistAndFlush(seller2);
        entityManager.persistAndFlush(product1);
        entityManager.persistAndFlush(product2);
        entityManager.persistAndFlush(stock1);
        entityManager.persistAndFlush(stock2);

        // When
        List<ProductStock> allStocks = productStockRepository.findAll();

        // Then
        assertTrue(allStocks.size() >= 2); // At least our 2 test stocks
        assertTrue(allStocks.stream().anyMatch(stock -> 
            stock.getProduct().getName().equals("Product X")));
        assertTrue(allStocks.stream().anyMatch(stock -> 
            stock.getProduct().getName().equals("Product Y")));
    }

    // Helper methods for creating test objects
    private Seller createTestSeller(String firstName, String lastName, String email) {
        Seller seller = new Seller();
        seller.setFirst_name(firstName);
        seller.setLast_name(lastName);
        seller.setEmail(email);
        seller.setPassword("password123");
        seller.setPhone_number("1234567890");
        return seller;
    }

    private Product createTestProduct(String name, String description, Double price) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        return product;
    }

    private ProductStock createTestProductStock(Product product, Seller seller, Integer amount) {
        ProductStock productStock = new ProductStock();
        productStock.setProduct(product);
        productStock.setSeller(seller);
        productStock.setAmount(amount);
        return productStock;
    }
}
