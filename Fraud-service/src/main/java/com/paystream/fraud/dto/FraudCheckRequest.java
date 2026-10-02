package com.paystream.fraud.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record FraudCheckRequest(

        @NotNull
        Long transactionId,

        @NotNull
        Long customerId,

        @NotNull
        Long merchantId,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal amount,

        @NotNull
        String currency

) {
}