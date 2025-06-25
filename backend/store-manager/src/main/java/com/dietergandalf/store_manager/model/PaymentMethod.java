package com.dietergandalf.store_manager.model;


public interface PaymentMethod {
	String getType();

	boolean initiatePayment();

	boolean verifyPayment();
}
