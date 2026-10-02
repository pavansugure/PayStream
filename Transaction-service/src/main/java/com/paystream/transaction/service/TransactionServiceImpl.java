package com.paystream.transaction.service;

import com.paystream.transaction.client.FraudClient;
import com.paystream.transaction.client.MerchantValidationClient;
import com.paystream.transaction.client.dto.FraudCheckRequest;
import com.paystream.transaction.client.dto.FraudCheckResponse;
import com.paystream.transaction.client.dto.MerchantStatusResponse;
import com.paystream.transaction.dto.CreateTransactionRequest;
import com.paystream.transaction.dto.TransactionResponse;
import com.paystream.transaction.entity.Transaction;
import com.paystream.transaction.entity.TransactionStatus;
import com.paystream.transaction.event.TransactionInitiatedEvent;
import com.paystream.transaction.exception.MerchantNotVerifiedException;
import com.paystream.transaction.messaging.TransactionEventPublisher;
import com.paystream.transaction.repository.TransactionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;
	private final MerchantValidationClient merchantValidationClient;
	private final FraudClient fraudClient;
	private final TransactionEventPublisher transactionEventPublisher;

	public TransactionServiceImpl(TransactionRepository transactionRepository,
			MerchantValidationClient merchantValidationClient, FraudClient fraudClient,
			TransactionEventPublisher transactionEventPublisher) {

		this.transactionRepository = transactionRepository;
		this.merchantValidationClient = merchantValidationClient;
		this.fraudClient = fraudClient;
		this.transactionEventPublisher = transactionEventPublisher;
	}

	/**
	 * Creates a new payment transaction.
	 *
	 * The customer ID is NOT accepted from the request body. It will come from the
	 * authenticated JWT.
	 */
	@Override
	public TransactionResponse createTransaction(CreateTransactionRequest request, Authentication authentication) {

		Long customerId = Long.valueOf(authentication.getName());

		MerchantStatusResponse merchant = merchantValidationClient.getMerchantStatus(request.merchantId());

		if (!merchant.verified()) {
			throw new MerchantNotVerifiedException("Merchant is not verified");
		}

		String transactionReference = "TXN-" + UUID.randomUUID();

		Instant now = Instant.now();

		Transaction transaction = new Transaction(transactionReference, customerId, request.merchantId(),
				request.amount(), request.currency(), TransactionStatus.FRAUD_CHECK_PENDING, now, now);

		Transaction savedTransaction = transactionRepository.save(transaction);

		TransactionInitiatedEvent event = new TransactionInitiatedEvent(UUID.randomUUID().toString(),
				savedTransaction.getTransactionReference(), savedTransaction.getCustomerId(),
				savedTransaction.getMerchantId(), savedTransaction.getAmount(), savedTransaction.getCurrency(),
				savedTransaction.getCreatedAt());

		transactionEventPublisher.publish(event);

		FraudCheckRequest fraudRequest = new FraudCheckRequest(savedTransaction.getId(),
				savedTransaction.getCustomerId(), savedTransaction.getMerchantId(), savedTransaction.getAmount(),
				savedTransaction.getCurrency());

		FraudCheckResponse fraudResponse = fraudClient.checkTransaction(fraudRequest);

		TransactionStatus finalStatus = mapFraudDecision(fraudResponse.decision());

		savedTransaction.setStatus(finalStatus);
		savedTransaction.setUpdatedAt(Instant.now());

		Transaction updatedTransaction = transactionRepository.save(savedTransaction);

		return toResponse(updatedTransaction);
	}

	private TransactionStatus mapFraudDecision(String decision) {

		return switch (decision) {

		case "APPROVED" -> TransactionStatus.APPROVED;

		case "DECLINED" -> TransactionStatus.DECLINED;

		case "MANUAL_REVIEW" -> TransactionStatus.MANUAL_REVIEW;

		default -> throw new IllegalStateException("Unknown fraud decision: " + decision);
		};
	}

	@Override
	public TransactionResponse getTransaction(String transactionReference, Authentication authentication) {

		Transaction transaction = transactionRepository.findByTransactionReference(transactionReference)
				.orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

		/*
		 * The JWT "sub" claim contains the authenticated user's ID.
		 *
		 * We use the authenticated identity instead of accepting a customerId from the
		 * request.
		 */
		Long customerId = Long.valueOf(authentication.getName());

		/*
		 * Ownership check:
		 *
		 * The customer can access the transaction only if the transaction belongs to
		 * the authenticated customer.
		 */
		if (!transaction.getCustomerId().equals(customerId)) {
			throw new org.springframework.security.access.AccessDeniedException(
					"You are not allowed to access this transaction");
		}

		return toResponse(transaction);
	}

	private TransactionResponse toResponse(Transaction transaction) {

		return new TransactionResponse(transaction.getTransactionReference(), transaction.getCustomerId(),
				transaction.getMerchantId(), transaction.getAmount(), transaction.getCurrency(),
				transaction.getStatus(), transaction.getCreatedAt());
	}
}