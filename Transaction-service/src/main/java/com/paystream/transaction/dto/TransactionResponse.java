package com.paystream.transaction.dto;

import com.paystream.transaction.entity.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Response DTO returned to the client after creating or retrieving a
 * transaction.
 *
 * A record keeps the response immutable and concise.
 */
public record TransactionResponse(

		String transactionReference,

		Long customerId,

		Long merchantId,

		BigDecimal amount,

		String currency,

		TransactionStatus status,

		Instant createdAt

) {
}