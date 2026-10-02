package com.paystream.ledger.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "ledger_entries", indexes = {
		@Index(name = "idx_ledger_transaction_reference", columnList = "transaction_reference") })
public class LedgerEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Business reference of the payment transaction.
	 */
	@Column(name = "transaction_reference", nullable = false, length = 100)
	private String transactionReference;

	/**
	 * Account that owns this ledger entry.
	 *
	 * For now: customerId or merchantId.
	 */
	@Column(name = "account_id", nullable = false)
	private Long accountId;

	@Enumerated(EnumType.STRING)
	@Column(name = "entry_type", nullable = false, length = 10)
	private LedgerEntryType entryType;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal amount;

	@Column(nullable = false, length = 3)
	private String currency;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	protected LedgerEntry() {
	}

	public LedgerEntry(String transactionReference, Long accountId, LedgerEntryType entryType, BigDecimal amount,
			String currency, Instant createdAt) {

		this.transactionReference = transactionReference;
		this.accountId = accountId;
		this.entryType = entryType;
		this.amount = amount;
		this.currency = currency;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public String getTransactionReference() {
		return transactionReference;
	}

	public Long getAccountId() {
		return accountId;
	}

	public LedgerEntryType getEntryType() {
		return entryType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getCurrency() {
		return currency;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
}