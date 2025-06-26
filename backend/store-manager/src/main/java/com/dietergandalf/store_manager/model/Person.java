package com.dietergandalf.store_manager.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Long person_id;
    String first_name;
    String last_name;
    String date_of_birth;
    String phone_number;
    @Embedded
    Address address;
    String email;
    String password;

	abstract Person login(String email, String password);

	abstract boolean register(String firstName, String lastName, String dateOfBirth, String phoneNumber,
			Address address, String email, String password);

	abstract boolean updateProfile(String firstName, String lastName, String dateOfBirth, String phone_number,
			Address address, String email);

	abstract boolean changePassword(String oldPassword, String newPassword);
}
