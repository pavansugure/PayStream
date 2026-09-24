package com.paystream.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MerchantNotVerifiedException.class)
	public ResponseEntity<ErrorResponse> handleMerchantNotVerified(MerchantNotVerifiedException exception) {

		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new ErrorResponse("MERCHANT_NOT_VERIFIED", exception.getMessage()));
	}

	@ExceptionHandler(MerchantNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleMerchantNotFound(MerchantNotFoundException exception) {

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse("MERCHANT_NOT_FOUND", exception.getMessage()));
	}

	public record ErrorResponse(String code, String message) {
	}
}