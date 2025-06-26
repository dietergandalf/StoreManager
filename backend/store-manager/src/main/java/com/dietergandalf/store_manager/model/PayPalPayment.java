package com.dietergandalf.store_manager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PayPalPayment implements PaymentMethod {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long paypal_id;
	String email;

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean initiatePayment() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyPayment() {
		// TODO Auto-generated method stub
		return false;
	}
}
