package com.paystream.transaction.service;

import com.paystream.transaction.client.MerchantValidationClient;
import com.paystream.transaction.client.dto.MerchantStatusResponse;
import com.paystream.transaction.dto.CreateTransactionRequest;
import com.paystream.transaction.dto.TransactionResponse;
import com.paystream.transaction.entity.Transaction;
import com.paystream.transaction.entity.TransactionStatus;
import com.paystream.transaction.exception.MerchantNotVerifiedException;
import com.paystream.transaction.repository.TransactionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;
	private final MerchantValidationClient merchantValidationClient;

	public TransactionServiceImpl(TransactionRepository transactionRepository,
			MerchantValidationClient merchantValidationClient) {

		this.transactionRepository = transactionRepository;
		this.merchantValidationClient = merchantValidationClient;
	}

	/**
	 * Creates a new payment transaction.
	 *
	 * The customer ID is NOT accepted from the request body. It will come from the
	 * authenticated JWT.
	 */
	@Override
	public TransactionResponse createTransaction(CreateTransactionRequest request, Authentication authentication) {

		/*
		 * The JWT "sub" claim contains the authenticated user's ID.
		 *
		 * We deliberately obtain the customer ID from Authentication instead of
		 * trusting a customerId supplied by the client.
		 */
		Long customerId = Long.valueOf(authentication.getName());

		MerchantStatusResponse merchant = merchantValidationClient.getMerchantStatus(request.merchantId());

		if (!merchant.verified()) {
			throw new MerchantNotVerifiedException("Merchant is not verified");
		}

		String transactionReference = "TXN-" + UUID.randomUUID();

		Instant now = Instant.now();

		Transaction transaction = new Transaction(transactionReference, customerId, request.merchantId(),
				request.amount(), request.currency(), TransactionStatus.INITIATED, now, now);

		Transaction savedTransaction = transactionRepository.save(transaction);

		return toResponse(savedTransaction);
	}

	@Override
	public TransactionResponse getTransaction(String transactionReference) {

		Transaction transaction = transactionRepository.findByTransactionReference(transactionReference)
				.orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

		return toResponse(transaction);
	}

	private TransactionResponse toResponse(Transaction transaction) {

		return new TransactionResponse(transaction.getTransactionReference(), transaction.getCustomerId(),
				transaction.getMerchantId(), transaction.getAmount(), transaction.getCurrency(),
				transaction.getStatus(), transaction.getCreatedAt());
	}
}