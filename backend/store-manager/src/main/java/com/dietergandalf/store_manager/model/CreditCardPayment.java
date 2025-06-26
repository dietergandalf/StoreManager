package com.dietergandalf.store_manager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CreditCardPayment implements PaymentMethod {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long credit_card_id;
	private String card_number;
	private String expiry_date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private Customer card_holder;

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
