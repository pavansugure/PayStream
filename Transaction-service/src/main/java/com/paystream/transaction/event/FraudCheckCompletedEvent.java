package com.paystream.transaction.event;

import com.paystream.transaction.entity.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record FraudCheckCompletedEvent(

		String eventId,

		String transactionReference,

		Long customerId,

		Long merchantId,

		BigDecimal amount,

		String currency,

		TransactionStatus transactionStatus,

		String fraudDecision,

		String reason,

		Instant occurredAt) {
}