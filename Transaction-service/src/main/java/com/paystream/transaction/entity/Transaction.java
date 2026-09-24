package com.paystream.transaction.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Represents a payment transaction in PayStream.
 *
 * This entity belongs exclusively to Transaction Service. Other microservices
 * will communicate with Transaction Service through APIs and events rather than
 * directly accessing this table.
 */
@Entity
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/*
	 * Unique business identifier exposed to clients and other services.
	 *
	 * We do not expose the database ID as the transaction reference.
	 */
	@Column(name = "transaction_reference", nullable = false, unique = true)
	private String transactionReference;

	/*
	 * ID of the customer who initiated the transaction.
	 *
	 * This corresponds to the "sub" claim in the JWT.
	 */
	@Column(name = "customer_id", nullable = false)
	private Long customerId;

	/*
	 * Merchant receiving the payment.
	 */
	@Column(name = "merchant_id", nullable = false)
	private Long merchantId;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal amount;

	@Column(nullable = false, length = 3)
	private String currency;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private TransactionStatus status;

	/*
	 * Used later for correlation across Transaction, Fraud, Ledger and Notification
	 * services.
	 */
	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	protected Transaction() {
		// Required by JPA.
	}

	public Transaction(String transactionReference, Long customerId, Long merchantId, BigDecimal amount,
			String currency, TransactionStatus status, Instant createdAt, Instant updatedAt) {

		this.transactionReference = transactionReference;
		this.customerId = customerId;
		this.merchantId = merchantId;
		this.amount = amount;
		this.currency = currency;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public String getTransactionReference() {
		return transactionReference;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getCurrency() {
		return currency;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
}