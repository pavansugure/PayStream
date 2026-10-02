package com.paystream.ledger.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "ledger_accounts", uniqueConstraints = {
		@UniqueConstraint(name = "uk_ledger_account_owner", columnNames = { "owner_id", "account_type" }) })
public class LedgerAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * ID of the business owner.
	 *
	 * CUSTOMER -> customerId MERCHANT -> merchantId
	 */
	@Column(name = "owner_id", nullable = false)
	private Long ownerId;

	@Enumerated(EnumType.STRING)
	@Column(name = "account_type", nullable = false, length = 20)
	private AccountType accountType;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal balance;

	@Column(nullable = false, length = 3)
	private String currency;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	protected LedgerAccount() {
	}

	public LedgerAccount(Long ownerId, AccountType accountType, BigDecimal balance, String currency, Instant createdAt,
			Instant updatedAt) {

		this.ownerId = ownerId;
		this.accountType = accountType;
		this.balance = balance;
		this.currency = currency;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public String getCurrency() {
		return currency;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
}