package com.dietergandalf.store_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dietergandalf.store_manager.model.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    // Additional query methods can be defined here if needed
}
