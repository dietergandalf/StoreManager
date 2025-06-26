package com.dietergandalf.store_manager.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer extends Person {
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "shopping_cart_id")
	private ShoppingCart cart;
	
	// For now, we'll store payment method as a simple enum or remove this field
	// since PaymentMethod is an interface and needs proper implementation
	// private PaymentMethod payment_method;

	@Override
	Person login(String email, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	boolean register(String firstName, String lastName, String dateOfBirth, String phoneNumber, Address address,
			String email, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean updateProfile(String firstName, String lastName, String dateOfBirth, String phone_number, Address address,
			String email) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean changePassword(String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		return false;
	}

}
