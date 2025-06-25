package com.dietergandalf.store_manager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Stand {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stand_id;
    private double price;
    private Seller stand_user;
    private double size;
    private Owner stand_owner;
}
