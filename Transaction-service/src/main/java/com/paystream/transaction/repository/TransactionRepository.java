package com.paystream.transaction.repository;

import com.paystream.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository responsible for persistence operations on Transaction.
 *
 * Spring Data JPA generates the implementation automatically at runtime.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	/**
	 * Finds a transaction using its business reference.
	 *
	 * The transaction reference is exposed to other services/clients, while the
	 * database ID remains an internal persistence detail.
	 */
	Optional<Transaction> findByTransactionReference(String transactionReference);

	/**
	 * Used to prevent duplicate business transaction references.
	 */
	boolean existsByTransactionReference(String transactionReference);
}