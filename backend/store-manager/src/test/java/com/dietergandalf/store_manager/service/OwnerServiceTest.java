package com.dietergandalf.store_manager.service;

import com.dietergandalf.store_manager.dto.OwnerDto;
import com.dietergandalf.store_manager.dto.RegisterRequestDto;
import com.dietergandalf.store_manager.dto.UpdateProfileRequestDto;
import com.dietergandalf.store_manager.model.Owner;
import com.dietergandalf.store_manager.repository.OwnerRepository;
import com.dietergandalf.store_manager.util.OwnerMapper;
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
class OwnerServiceTest {

    @Mock
    private OwnerRepository ownerRepository;
    
    @Mock
    private OwnerMapper ownerMapper;

    private OwnerService ownerService;

    @BeforeEach
    void setUp() {
        ownerService = new OwnerService(ownerRepository, ownerMapper);
    }

    @Test
    void getAllOwners_ShouldReturnAllOwners() {
        // Given
        Owner owner1 = createTestOwner(1L, "John", "Doe");
        Owner owner2 = createTestOwner(2L, "Jane", "Smith");
        List<Owner> owners = Arrays.asList(owner1, owner2);
        
        OwnerDto ownerDto1 = createTestOwnerDto(1L, "John", "Doe");
        OwnerDto ownerDto2 = createTestOwnerDto(2L, "Jane", "Smith");
        List<OwnerDto> ownerDtos = Arrays.asList(ownerDto1, ownerDto2);

        when(ownerRepository.findAll()).thenReturn(owners);
        when(ownerMapper.toDtoList(owners)).thenReturn(ownerDtos);

        // When
        List<OwnerDto> result = ownerService.getAllOwners();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        
        verify(ownerRepository).findAll();
        verify(ownerMapper).toDtoList(owners);
    }

    @Test
    void getAllOwners_WhenNoOwners_ShouldReturnEmptyList() {
        // Given
        List<Owner> emptyOwners = Arrays.asList();
        List<OwnerDto> emptyOwnerDtos = Arrays.asList();

        when(ownerRepository.findAll()).thenReturn(emptyOwners);
        when(ownerMapper.toDtoList(emptyOwners)).thenReturn(emptyOwnerDtos);

        // When
        List<OwnerDto> result = ownerService.getAllOwners();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(ownerRepository).findAll();
        verify(ownerMapper).toDtoList(emptyOwners);
    }

    @Test
    void createOwner_WithValidData_ShouldCreateOwner() {
        // Given
        RegisterRequestDto registerRequest = RegisterRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .phoneNumber("1234567890")
                .dateOfBirth("1980-01-01")
                .build();

        Owner owner = createTestOwner(null, "John", "Doe");
        Owner savedOwner = createTestOwner(1L, "John", "Doe");
        OwnerDto ownerDto = createTestOwnerDto(1L, "John", "Doe");

        when(ownerMapper.fromRegisterRequest(registerRequest)).thenReturn(owner);
        when(ownerRepository.save(owner)).thenReturn(savedOwner);
        when(ownerMapper.toDto(savedOwner)).thenReturn(ownerDto);

        // When
        OwnerDto result = ownerService.createOwner(registerRequest);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getPersonId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        
        verify(ownerMapper).fromRegisterRequest(registerRequest);
        verify(ownerRepository).save(owner);
        verify(ownerMapper).toDto(savedOwner);
    }

    @Test
    void createOwner_WithNullRequest_ShouldHandleGracefully() {
        // Given
        RegisterRequestDto registerRequest = null;
        Owner owner = new Owner();

        when(ownerMapper.fromRegisterRequest(registerRequest)).thenReturn(owner);
        when(ownerRepository.save(owner)).thenReturn(owner);
        when(ownerMapper.toDto(owner)).thenReturn(new OwnerDto());

        // When
        OwnerDto result = ownerService.createOwner(registerRequest);

        // Then
        assertNotNull(result);
        verify(ownerMapper).fromRegisterRequest(registerRequest);
        verify(ownerRepository).save(owner);
        verify(ownerMapper).toDto(owner);
    }

    @Test
    void getOwnerById_WhenOwnerExists_ShouldReturnOwner() {
        // Given
        Long ownerId = 1L;
        Owner owner = createTestOwner(ownerId, "John", "Doe");
        OwnerDto ownerDto = createTestOwnerDto(ownerId, "John", "Doe");

        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(ownerMapper.toDto(owner)).thenReturn(ownerDto);

        // When
        OwnerDto result = ownerService.getOwnerById(ownerId);

        // Then
        assertNotNull(result);
        assertEquals(ownerId, result.getPersonId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        
        verify(ownerRepository).findById(ownerId);
        verify(ownerMapper).toDto(owner);
    }

    @Test
    void getOwnerById_WhenOwnerNotExists_ShouldReturnMappedResult() {
        // Given
        Long ownerId = 999L;
        
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.empty());
        when(ownerMapper.toDto(null)).thenReturn(null);

        // When
        OwnerDto result = ownerService.getOwnerById(ownerId);

        // Then
        assertNull(result);
        verify(ownerRepository).findById(ownerId);
        verify(ownerMapper).toDto(null);
    }

    @Test
    void updateOwner_WhenOwnerExists_ShouldUpdateOwner() {
        // Given
        Long ownerId = 1L;
        UpdateProfileRequestDto updateRequest = UpdateProfileRequestDto.builder()
                .firstName("Updated John")
                .lastName("Updated Doe")
                .email("updated.john@example.com")
                .phoneNumber("0987654321")
                .build();

        Owner existingOwner = createTestOwner(ownerId, "John", "Doe");
        Owner updatedOwner = createTestOwner(ownerId, "Updated John", "Updated Doe");
        OwnerDto ownerDto = createTestOwnerDto(ownerId, "Updated John", "Updated Doe");

        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(existingOwner));
        when(ownerRepository.save(existingOwner)).thenReturn(updatedOwner);
        when(ownerMapper.toDto(updatedOwner)).thenReturn(ownerDto);

        // When
        OwnerDto result = ownerService.updateOwner(ownerId, updateRequest);

        // Then
        assertNotNull(result);
        assertEquals("Updated John", result.getFirstName());
        assertEquals("Updated Doe", result.getLastName());
        
        verify(ownerRepository).findById(ownerId);
        verify(ownerMapper).updateOwnerFromDto(existingOwner, updateRequest);
        verify(ownerRepository).save(existingOwner);
        verify(ownerMapper).toDto(updatedOwner);
    }

    @Test
    void updateOwner_WhenOwnerNotExists_ShouldReturnNull() {
        // Given
        Long ownerId = 999L;
        UpdateProfileRequestDto updateRequest = UpdateProfileRequestDto.builder()
                .firstName("Updated John")
                .build();

        when(ownerRepository.findById(ownerId)).thenReturn(Optional.empty());

        // When
        OwnerDto result = ownerService.updateOwner(ownerId, updateRequest);

        // Then
        assertNull(result);
        verify(ownerRepository).findById(ownerId);
        verifyNoMoreInteractions(ownerRepository, ownerMapper);
    }

    @Test
    void deleteOwner_WhenOwnerExists_ShouldReturnTrue() {
        // Given
        Long ownerId = 1L;
        when(ownerRepository.existsById(ownerId)).thenReturn(true);

        // When
        boolean result = ownerService.deleteOwner(ownerId);

        // Then
        assertTrue(result);
        verify(ownerRepository).existsById(ownerId);
        verify(ownerRepository).deleteById(ownerId);
    }

    @Test
    void deleteOwner_WhenOwnerNotExists_ShouldReturnFalse() {
        // Given
        Long ownerId = 999L;
        when(ownerRepository.existsById(ownerId)).thenReturn(false);

        // When
        boolean result = ownerService.deleteOwner(ownerId);

        // Then
        assertFalse(result);
        verify(ownerRepository).existsById(ownerId);
        verify(ownerRepository, never()).deleteById(anyLong());
    }

    // Helper methods for creating test objects
    private Owner createTestOwner(Long id, String firstName, String lastName) {
        Owner owner = new Owner();
        owner.setPerson_id(id);
        owner.setFirst_name(firstName);
        owner.setLast_name(lastName);
        owner.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com");
        owner.setPhone_number("1234567890");
        return owner;
    }

    private OwnerDto createTestOwnerDto(Long id, String firstName, String lastName) {
        return OwnerDto.builder()
                .personId(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com")
                .phoneNumber("1234567890")
                .build();
    }
}
