package com.dietergandalf.store_manager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductStock {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long product_stock_id;
	private Product product;
	private int amount;
}
