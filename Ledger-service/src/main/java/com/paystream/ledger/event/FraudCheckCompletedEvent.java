package com.paystream.ledger.event;

import java.math.BigDecimal;
import java.time.Instant;

public record FraudCheckCompletedEvent(

		String eventId,

		String transactionReference,

		Long customerId,

		Long merchantId,

		BigDecimal amount,

		String currency,

		String transactionStatus,

		String fraudDecision,

		String reason,

		Instant occurredAt) {
}