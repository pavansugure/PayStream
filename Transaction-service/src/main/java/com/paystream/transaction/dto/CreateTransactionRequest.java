package com.paystream.transaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Request DTO used when a customer initiates a payment transaction.
 *
 * Java 21 record is appropriate here because this object only carries request
 * data and does not require mutable state.
 */
public record CreateTransactionRequest(

		@NotNull @Positive Long merchantId,

		@NotNull @DecimalMin(value = "0.01") BigDecimal amount,

		@NotNull String currency

) {
}