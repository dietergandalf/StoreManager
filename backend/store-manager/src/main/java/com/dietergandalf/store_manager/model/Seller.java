package com.dietergandalf.store_manager.model;

import java.util.List;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Seller extends Person {
	List<ProductStock> products;
	Stand stand;

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
