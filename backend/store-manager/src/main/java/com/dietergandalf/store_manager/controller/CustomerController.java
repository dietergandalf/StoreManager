package com.dietergandalf.store_manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dietergandalf.store_manager.dto.AddToCartRequestDto;
import com.dietergandalf.store_manager.dto.CustomerDto;
import com.dietergandalf.store_manager.dto.ProductStockDto;
import com.dietergandalf.store_manager.dto.RegisterRequestDto;
import com.dietergandalf.store_manager.dto.ShoppingCartDto;
import com.dietergandalf.store_manager.dto.UpdateProfileRequestDto;
import com.dietergandalf.store_manager.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Customer Management", description = "Operations related to customers and their shopping experience")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Get all customers", description = "Retrieves a list of all registered customers in the system")
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Create a new customer", description = "Registers a new customer with the provided information. Email must be unique.")
    @PostMapping("/customers")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody RegisterRequestDto registerRequest) {
        try {
            CustomerDto createdCustomer = customerService.createCustomer(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get customer by ID", description = "Retrieves a specific customer by their unique identifier")
    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        CustomerDto customer = customerService.getCustomerById(id);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Update an existing customer", description = "Updates an existing customer with new information")
    @PutMapping("/customers/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @RequestBody UpdateProfileRequestDto updateRequest) {
        CustomerDto updatedCustomer = customerService.updateCustomer(id, updateRequest);
        if (updatedCustomer != null) {
            return ResponseEntity.ok(updatedCustomer);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a customer", description = "Removes a customer from the system. This operation cannot be undone.")
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        boolean deleted = customerService.deleteCustomer(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get all available products", description = "Retrieves all products that are currently in stock and available for purchase")
    @GetMapping("/customers/products")
    public ResponseEntity<List<ProductStockDto>> getAllAvailableProducts() {
        List<ProductStockDto> products = customerService.getAllAvailableProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Add product to cart", description = "Adds a specified quantity of a product to the customer's shopping cart")
    @PostMapping("/customers/{customerId}/cart")
    public ResponseEntity<ShoppingCartDto> addToCart(@PathVariable Long customerId, @RequestBody AddToCartRequestDto addToCartRequest) {
        try {
            ShoppingCartDto cart = customerService.addToCart(customerId, addToCartRequest);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get customer's shopping cart", description = "Retrieves the current contents of the customer's shopping cart")
    @GetMapping("/customers/{customerId}/cart")
    public ResponseEntity<ShoppingCartDto> getCart(@PathVariable Long customerId) {
        ShoppingCartDto cart = customerService.getCart(customerId);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Clear shopping cart", description = "Removes all items from the customer's shopping cart")
    @DeleteMapping("/customers/{customerId}/cart")
    public ResponseEntity<Void> clearCart(@PathVariable Long customerId) {
        boolean cleared = customerService.clearCart(customerId);
        if (cleared) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Remove item from cart", description = "Removes a specific item from the customer's shopping cart")
    @DeleteMapping("/customers/{customerId}/cart/items/{cartItemId}")
    public ResponseEntity<ShoppingCartDto> removeFromCart(@PathVariable Long customerId, @PathVariable Long cartItemId) {
        try {
            ShoppingCartDto cart = customerService.removeFromCart(customerId, cartItemId);
            if (cart != null) {
                return ResponseEntity.ok(cart);
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Update cart item quantity", description = "Updates the quantity of a specific item in the customer's shopping cart")
    @PutMapping("/customers/{customerId}/cart/items/{cartItemId}")
    public ResponseEntity<ShoppingCartDto> updateCartItemQuantity(@PathVariable Long customerId, 
                                                                 @PathVariable Long cartItemId, 
                                                                 @RequestParam Integer quantity) {
        try {
            ShoppingCartDto cart = customerService.updateCartItemQuantity(customerId, cartItemId, quantity);
            if (cart != null) {
                return ResponseEntity.ok(cart);
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
