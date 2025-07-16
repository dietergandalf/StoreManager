package com.dietergandalf.store_manager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dietergandalf.store_manager.dto.OwnerDto;
import com.dietergandalf.store_manager.dto.RegisterRequestDto;
import com.dietergandalf.store_manager.dto.UpdateProfileRequestDto;
import com.dietergandalf.store_manager.model.Owner;
import com.dietergandalf.store_manager.repository.OwnerRepository;
import com.dietergandalf.store_manager.util.OwnerMapper;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    @Autowired
    public OwnerService(OwnerRepository ownerRepository, OwnerMapper ownerMapper) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
    }

    public List<OwnerDto> getAllOwners() {
        List<Owner> owners = ownerRepository.findAll();
        return ownerMapper.toDtoList(owners);
    }

    public OwnerDto createOwner(RegisterRequestDto registerRequest) {
        Owner owner = ownerMapper.fromRegisterRequest(registerRequest);
        Owner savedOwner = ownerRepository.save(owner);
        return ownerMapper.toDto(savedOwner);
    }

    public OwnerDto getOwnerById(Long id) {
        Owner owner = ownerRepository.findById(id).orElse(null);
        return ownerMapper.toDto(owner);
    }

    public OwnerDto updateOwner(Long id, UpdateProfileRequestDto updateRequest) {
        Owner existingOwner = ownerRepository.findById(id).orElse(null);
        if (existingOwner != null) {
            ownerMapper.updateOwnerFromDto(existingOwner, updateRequest);
            Owner updatedOwner = ownerRepository.save(existingOwner);
            return ownerMapper.toDto(updatedOwner);
        }
        return null;
    }

    public boolean deleteOwner(Long id) {
        if (ownerRepository.existsById(id)) {
            ownerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
