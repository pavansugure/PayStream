package com.paystream.transaction.entity;

/**
 * Represents the lifecycle state of a payment transaction.
 *
 * The status will evolve as the transaction moves through
 * fraud detection and subsequent payment processing.
 */
public enum TransactionStatus {

    INITIATED,
    FRAUD_CHECK_PENDING,
    APPROVED,
    DECLINED,
    MANUAL_REVIEW,
    COMPLETED,
    FAILED
}