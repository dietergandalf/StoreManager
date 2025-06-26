package com.dietergandalf.store_manager.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ShoppingCart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long cart_id;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "cart")
	private Customer customer;
	
	@ManyToMany
	@JoinTable(
		name = "cart_products",
		joinColumns = @JoinColumn(name = "cart_id"),
		inverseJoinColumns = @JoinColumn(name = "product_stock_id")
	)
	private List<ProductStock> products;
}
