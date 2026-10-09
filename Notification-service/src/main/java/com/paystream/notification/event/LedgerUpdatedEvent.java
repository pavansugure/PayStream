package com.paystream.notification.event;

import java.math.BigDecimal;
import java.time.Instant;

public record LedgerUpdatedEvent(

		String eventId,

		String transactionReference,

		Long customerId,

		Long merchantId,

		BigDecimal amount,

		String currency,

		String status,

		Instant occurredAt) {
}