package com.paystream.transaction.controller;

import com.paystream.transaction.dto.CreateTransactionRequest;
import com.paystream.transaction.dto.TransactionResponse;
import com.paystream.transaction.service.TransactionService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	/*
	 * Only authenticated customers can initiate a payment.
	 *
	 * @PreAuthorize checks the role extracted from the JWT. Our SecurityConfig
	 * converts:
	 *
	 * role = CUSTOMER
	 *
	 * into: ROLE_CUSTOMER
	 *
	 * Therefore hasRole("CUSTOMER") is satisfied.
	 */
	@PostMapping
	@PreAuthorize("hasRole('CUSTOMER')")
	@ResponseStatus(HttpStatus.CREATED)
	public TransactionResponse createTransaction(@Valid @RequestBody CreateTransactionRequest request,
			Authentication authentication) {

		return transactionService.createTransaction(request, authentication);
	}
}