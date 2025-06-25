package com.dietergandalf.store_manager.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer extends Person {
	private ShoppingCart cart;
	private PaymentMethod payment_method;

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
