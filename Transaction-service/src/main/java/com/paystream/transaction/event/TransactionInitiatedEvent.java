package com.paystream.transaction.event;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionInitiatedEvent(
        String eventId,
        String transactionReference,
        Long customerId,
        Long merchantId,
        BigDecimal amount,
        String currency,
        Instant occurredAt
) {
}