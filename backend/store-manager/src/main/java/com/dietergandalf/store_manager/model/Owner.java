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
public class Owner extends Person {
    List<Stand> stands;

	public List<Stand> getAvailableStands() {
        //TODO: Logic to return available stands
		return stands;
    }

    public double getRent() {
        //TODO: Logic to calculate and return the rent for the owner
		return 0;
    }

    public boolean hasAvailableStand(){
        //TODO: Logic to check if the owner has any available stands
        return getAvailableStands().size() > 0;
    }

    @Override
	public Owner login(String email, String password) {
        //TODO: Logic for owner login
		return this;
    }
    @Override
    public boolean register(String firstName, String lastName, String dateOfBirth, String phoneNumber, Address address, String email, String password) {
        //TODO: Logic for owner registration
		return false;
    }
    @Override
    public boolean updateProfile(String firstName, String lastName, String dateOfBirth, String phone_number, Address address, String email) {
        //TODO: Logic to update owner's profile
		return false;
    }
    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        //TODO: Logic to change owner's password
        return true;
    }
}