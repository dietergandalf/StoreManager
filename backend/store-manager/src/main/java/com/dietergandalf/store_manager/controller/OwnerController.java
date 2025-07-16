
package com.dietergandalf.store_manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dietergandalf.store_manager.dto.OwnerDto;
import com.dietergandalf.store_manager.dto.RegisterRequestDto;
import com.dietergandalf.store_manager.dto.UpdateProfileRequestDto;
import com.dietergandalf.store_manager.service.OwnerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Owner Management", description = "Operations related to store owners and their profile management")
public class OwnerController {
    private final OwnerService ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @Operation(summary = "Get all owners", description = "Retrieves a list of all registered store owners in the system")
    @GetMapping("/owners")
    public ResponseEntity<List<OwnerDto>> getAllOwners() {
        List<OwnerDto> owners = ownerService.getAllOwners();
        return ResponseEntity.ok(owners);
    }

    @Operation(summary = "Create a new owner", description = "Creates a new store owner with the provided information. Email must be unique.")
    @PostMapping("/owners")
    public ResponseEntity<OwnerDto> createOwner(@RequestBody RegisterRequestDto registerRequest) {
        OwnerDto createdOwner = ownerService.createOwner(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOwner);
    }

    @Operation(summary = "Get owner by ID", description = "Retrieves a specific store owner by their unique identifier")
    @GetMapping("/owners/{id}")
    public ResponseEntity<OwnerDto> getOwnerById(@PathVariable Long id) {
        OwnerDto owner = ownerService.getOwnerById(id);
        if (owner != null) {
            return ResponseEntity.ok(owner);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Update an existing owner", description = "Updates an existing store owner with new information. Email must remain unique.")
    @PutMapping("/owners/{id}")
    public ResponseEntity<OwnerDto> updateOwner(@PathVariable Long id, @RequestBody UpdateProfileRequestDto updateRequest) {
        OwnerDto updatedOwner = ownerService.updateOwner(id, updateRequest);
        if (updatedOwner != null) {
            return ResponseEntity.ok(updatedOwner);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete an owner", description = "Removes a store owner from the system. This operation cannot be undone. Cannot delete owners with active stands.")
    @DeleteMapping("/owners/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        boolean deleted = ownerService.deleteOwner(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
