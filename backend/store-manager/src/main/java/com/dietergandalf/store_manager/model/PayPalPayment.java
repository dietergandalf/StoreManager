package com.dietergandalf.store_manager.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PayPalPayment implements PaymentMethod {
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
