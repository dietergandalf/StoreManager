package com.dietergandalf.store_manager.repository;

import com.dietergandalf.store_manager.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void save_ShouldPersistCustomer() {
        // Given
        Customer customer = new Customer();
        customer.setFirst_name("John");
        customer.setLast_name("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("password123");

        // When
        Customer savedCustomer = customerRepository.save(customer);

        // Then
        assertNotNull(savedCustomer);
        assertNotNull(savedCustomer.getPerson_id());
        assertEquals("John", savedCustomer.getFirst_name());
        assertEquals("Doe", savedCustomer.getLast_name());
        assertEquals("john.doe@example.com", savedCustomer.getEmail());
    }

    @Test
    void findById_WhenCustomerExists_ShouldReturnCustomer() {
        // Given
        Customer customer = new Customer();
        customer.setFirst_name("Jane");
        customer.setLast_name("Smith");
        customer.setEmail("jane.smith@example.com");
        customer.setPassword("password123");
        
        Customer savedCustomer = entityManager.persistAndFlush(customer);

        // When
        Optional<Customer> found = customerRepository.findById(savedCustomer.getPerson_id());

        // Then
        assertTrue(found.isPresent());
        assertEquals(savedCustomer.getPerson_id(), found.get().getPerson_id());
        assertEquals("Jane", found.get().getFirst_name());
        assertEquals("Smith", found.get().getLast_name());
    }

    @Test
    void findById_WhenCustomerNotExists_ShouldReturnEmpty() {
        // When
        Optional<Customer> found = customerRepository.findById(999L);

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void deleteById_ShouldRemoveCustomer() {
        // Given
        Customer customer = new Customer();
        customer.setFirst_name("Bob");
        customer.setLast_name("Johnson");
        customer.setEmail("bob.johnson@example.com");
        customer.setPassword("password123");
        
        Customer savedCustomer = entityManager.persistAndFlush(customer);
        Long customerId = savedCustomer.getPerson_id();

        // When
        customerRepository.deleteById(customerId);
        entityManager.flush();

        // Then
        Optional<Customer> found = customerRepository.findById(customerId);
        assertFalse(found.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllCustomers() {
        // Given
        Customer customer1 = new Customer();
        customer1.setFirst_name("Alice");
        customer1.setLast_name("Brown");
        customer1.setEmail("alice.brown@example.com");
        customer1.setPassword("password123");

        Customer customer2 = new Customer();
        customer2.setFirst_name("Charlie");
        customer2.setLast_name("Wilson");
        customer2.setEmail("charlie.wilson@example.com");
        customer2.setPassword("password123");

        entityManager.persistAndFlush(customer1);
        entityManager.persistAndFlush(customer2);

        // When
        var customers = customerRepository.findAll();

        // Then
        assertEquals(2, customers.size());
    }
}
