package com.dietergandalf.store_manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dietergandalf.store_manager.dto.CreateProductRequestDto;
import com.dietergandalf.store_manager.dto.ProductStockDto;
import com.dietergandalf.store_manager.dto.RegisterRequestDto;
import com.dietergandalf.store_manager.dto.SellerDto;
import com.dietergandalf.store_manager.dto.UpdateProfileRequestDto;
import com.dietergandalf.store_manager.dto.UpdateProductStockRequestDto;
import com.dietergandalf.store_manager.service.SellerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Seller Management", description = "Operations related to sellers and their product management")
public class SellerController {
    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Operation(summary = "Get all sellers", description = "Retrieves a list of all registered sellers in the system")
    @GetMapping("/sellers")
    public ResponseEntity<List<SellerDto>> getAllSellers() {
        List<SellerDto> sellers = sellerService.getAllSellers();
        return ResponseEntity.ok(sellers);
    }

    @Operation(summary = "Create a new seller", description = "Registers a new seller with the provided information. Email must be unique.")
    @PostMapping("/sellers")
    public ResponseEntity<SellerDto> createSeller(@RequestBody RegisterRequestDto registerRequest) {
        try {
            SellerDto createdSeller = sellerService.createSeller(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSeller);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get seller by ID", description = "Retrieves a specific seller by their unique identifier")
    @GetMapping("/sellers/{id}")
    public ResponseEntity<SellerDto> getSellerById(@PathVariable Long id) {
        SellerDto seller = sellerService.getSellerById(id);
        if (seller != null) {
            return ResponseEntity.ok(seller);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Update an existing seller", description = "Updates an existing seller with new information")
    @PutMapping("/sellers/{id}")
    public ResponseEntity<SellerDto> updateSeller(@PathVariable Long id, @RequestBody UpdateProfileRequestDto updateRequest) {
        SellerDto updatedSeller = sellerService.updateSeller(id, updateRequest);
        if (updatedSeller != null) {
            return ResponseEntity.ok(updatedSeller);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a seller", description = "Removes a seller from the system. This operation cannot be undone.")
    @DeleteMapping("/sellers/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        boolean deleted = sellerService.deleteSeller(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Add a new product", description = "Allows a seller to add a new product to their inventory with initial stock")
    @PostMapping("/sellers/{sellerId}/products")
    public ResponseEntity<ProductStockDto> addProduct(@PathVariable Long sellerId, @RequestBody CreateProductRequestDto productRequest) {
        try {
            ProductStockDto createdProduct = sellerService.addProduct(sellerId, productRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get seller's products", description = "Retrieves all products that belong to a specific seller")
    @GetMapping("/sellers/{sellerId}/products")
    public ResponseEntity<List<ProductStockDto>> getSellerProducts(@PathVariable Long sellerId) {
        List<ProductStockDto> products = sellerService.getSellerProducts(sellerId);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Update product stock", description = "Updates the stock quantity for a specific product")
    @PutMapping("/sellers/{sellerId}/products/{productStockId}/stock")
    public ResponseEntity<ProductStockDto> updateProductStock(@PathVariable Long sellerId, 
                                                             @PathVariable Long productStockId, 
                                                             @RequestBody UpdateProductStockRequestDto updateRequest) {
        try {
            ProductStockDto updatedProduct = null;
            
            if (updateRequest.getQuantity() != null) {
                updatedProduct = sellerService.updateProductStock(sellerId, productStockId, updateRequest.getQuantity());
            }
            
            if (updateRequest.getPrice() != null) {
                updatedProduct = sellerService.updateProductPrice(sellerId, productStockId, updateRequest.getPrice());
            }
            
            if (updatedProduct != null) {
                return ResponseEntity.ok(updatedProduct);
            }
            
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Remove product", description = "Removes a product from the seller's inventory. This operation cannot be undone.")
    @DeleteMapping("/sellers/{sellerId}/products/{productStockId}")
    public ResponseEntity<Void> removeProduct(@PathVariable Long sellerId, @PathVariable Long productStockId) {
        try {
            boolean removed = sellerService.removeProduct(sellerId, productStockId);
            if (removed) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Update product price", description = "Updates the price for a specific product")
    @PutMapping("/sellers/{sellerId}/products/{productStockId}/price")
    public ResponseEntity<ProductStockDto> updateProductPrice(@PathVariable Long sellerId, 
                                                             @PathVariable Long productStockId, 
                                                             @RequestParam Double price) {
        try {
            ProductStockDto updatedProduct = sellerService.updateProductPrice(sellerId, productStockId, price);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
