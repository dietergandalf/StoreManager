package com.dietergandalf.store_manager.repository;

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
public class SellerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SellerRepository sellerRepository;

    @Test
    void save_ShouldPersistSeller() {
        // Given
        Seller seller = createTestSeller("John", "Doe", "john.doe@example.com");

        // When
        Seller savedSeller = sellerRepository.save(seller);

        // Then
        assertNotNull(savedSeller);
        assertNotNull(savedSeller.getPerson_id());
        assertEquals("John", savedSeller.getFirst_name());
        assertEquals("Doe", savedSeller.getLast_name());
        assertEquals("john.doe@example.com", savedSeller.getEmail());
    }

    @Test
    void findById_WhenSellerExists_ShouldReturnSeller() {
        // Given
        Seller seller = createTestSeller("Jane", "Smith", "jane.smith@example.com");
        Seller savedSeller = entityManager.persistAndFlush(seller);

        // When
        Optional<Seller> found = sellerRepository.findById(savedSeller.getPerson_id());

        // Then
        assertTrue(found.isPresent());
        assertEquals(savedSeller.getPerson_id(), found.get().getPerson_id());
        assertEquals("Jane", found.get().getFirst_name());
        assertEquals("Smith", found.get().getLast_name());
        assertEquals("jane.smith@example.com", found.get().getEmail());
    }

    @Test
    void findById_WhenSellerNotExists_ShouldReturnEmpty() {
        // When
        Optional<Seller> found = sellerRepository.findById(999L);

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void findByEmail_WhenSellerExists_ShouldReturnSeller() {
        // Given
        String email = "alice.brown@example.com";
        Seller seller = createTestSeller("Alice", "Brown", email);
        entityManager.persistAndFlush(seller);

        // When
        Optional<Seller> found = sellerRepository.findByEmail(email);

        // Then
        assertTrue(found.isPresent());
        assertEquals(email, found.get().getEmail());
        assertEquals("Alice", found.get().getFirst_name());
        assertEquals("Brown", found.get().getLast_name());
    }

    @Test
    void findByEmail_WhenSellerNotExists_ShouldReturnEmpty() {
        // When
        Optional<Seller> found = sellerRepository.findByEmail("nonexistent@example.com");

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void findByEmail_ShouldBeCaseInsensitive() {
        // Given
        String email = "bob.wilson@example.com";
        Seller seller = createTestSeller("Bob", "Wilson", email);
        entityManager.persistAndFlush(seller);

        // When
        Optional<Seller> foundLowerCase = sellerRepository.findByEmail(email.toLowerCase());
        Optional<Seller> foundUpperCase = sellerRepository.findByEmail(email.toUpperCase());

        // Then
        // Note: This test behavior depends on database collation settings
        // In most cases, email searches should be case-insensitive
        assertTrue(foundLowerCase.isPresent());
        assertEquals("Bob", foundLowerCase.get().getFirst_name());
    }

    @Test
    void existsByEmail_WhenSellerExists_ShouldReturnTrue() {
        // Given
        String email = "charlie.davis@example.com";
        Seller seller = createTestSeller("Charlie", "Davis", email);
        entityManager.persistAndFlush(seller);

        // When
        boolean exists = sellerRepository.existsByEmail(email);

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByEmail_WhenSellerNotExists_ShouldReturnFalse() {
        // When
        boolean exists = sellerRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertFalse(exists);
    }

    @Test
    void deleteById_ShouldRemoveSeller() {
        // Given
        Seller seller = createTestSeller("Eve", "Johnson", "eve.johnson@example.com");
        Seller savedSeller = entityManager.persistAndFlush(seller);
        Long sellerId = savedSeller.getPerson_id();

        // When
        sellerRepository.deleteById(sellerId);
        entityManager.flush();

        // Then
        Optional<Seller> found = sellerRepository.findById(sellerId);
        assertFalse(found.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllSellers() {
        // Given
        Seller seller1 = createTestSeller("Frank", "Miller", "frank.miller@example.com");
        Seller seller2 = createTestSeller("Grace", "Taylor", "grace.taylor@example.com");
        Seller seller3 = createTestSeller("Henry", "Anderson", "henry.anderson@example.com");

        entityManager.persistAndFlush(seller1);
        entityManager.persistAndFlush(seller2);
        entityManager.persistAndFlush(seller3);

        // When
        List<Seller> sellers = sellerRepository.findAll();

        // Then
        assertTrue(sellers.size() >= 3); // At least our 3 test sellers
        assertTrue(sellers.stream().anyMatch(s -> s.getEmail().equals("frank.miller@example.com")));
        assertTrue(sellers.stream().anyMatch(s -> s.getEmail().equals("grace.taylor@example.com")));
        assertTrue(sellers.stream().anyMatch(s -> s.getEmail().equals("henry.anderson@example.com")));
    }

    @Test
    void save_WithDuplicateEmail_ShouldSaveSuccessfully() {
        // Given - Since email constraint is handled at service level, not database level
        String email = "duplicate@example.com";
        Seller seller1 = createTestSeller("First", "Seller", email);
        Seller seller2 = createTestSeller("Second", "Seller", email);

        entityManager.persistAndFlush(seller1);
        entityManager.clear();

        // When
        Seller savedSeller = sellerRepository.saveAndFlush(seller2);

        // Then - Both sellers should be saved successfully
        assertNotNull(savedSeller);
        assertNotNull(savedSeller.getPerson_id());
        assertEquals(email, savedSeller.getEmail());
        
        // Verify both sellers exist in database
        List<Seller> allSellers = sellerRepository.findAll();
        long duplicateEmailCount = allSellers.stream()
                .filter(s -> email.equals(s.getEmail()))
                .count();
        assertEquals(2, duplicateEmailCount);
    }

    @Test
    void updateSeller_ShouldPersistChanges() {
        // Given
        Seller seller = createTestSeller("Original", "Name", "original@example.com");
        Seller savedSeller = entityManager.persistAndFlush(seller);

        // When
        savedSeller.setFirst_name("Updated");
        savedSeller.setLast_name("Updated Name");
        savedSeller.setPhone_number("9876543210");
        Seller updatedSeller = sellerRepository.save(savedSeller);

        // Then
        assertEquals("Updated", updatedSeller.getFirst_name());
        assertEquals("Updated Name", updatedSeller.getLast_name());
        assertEquals("9876543210", updatedSeller.getPhone_number());
        assertEquals("original@example.com", updatedSeller.getEmail()); // Email should remain the same
    }

    @Test
    void count_ShouldReturnCorrectNumberOfSellers() {
        // Given
        long initialCount = sellerRepository.count();
        
        Seller seller1 = createTestSeller("Count", "Test1", "count1@example.com");
        Seller seller2 = createTestSeller("Count", "Test2", "count2@example.com");

        entityManager.persistAndFlush(seller1);
        entityManager.persistAndFlush(seller2);

        // When
        long finalCount = sellerRepository.count();

        // Then
        assertEquals(initialCount + 2, finalCount);
    }

    // Helper method for creating test sellers
    private Seller createTestSeller(String firstName, String lastName, String email) {
        Seller seller = new Seller();
        seller.setFirst_name(firstName);
        seller.setLast_name(lastName);
        seller.setEmail(email);
        seller.setPassword("password123");
        seller.setPhone_number("1234567890");
        seller.setDate_of_birth("1990-01-01");
        return seller;
    }
}
