package com.paystream.transaction.exception;

public class MerchantNotVerifiedException extends RuntimeException {

	public MerchantNotVerifiedException(String message) {
		super(message);
	}
}