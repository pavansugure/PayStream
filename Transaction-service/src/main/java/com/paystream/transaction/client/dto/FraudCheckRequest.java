package com.paystream.transaction.client.dto;

import java.math.BigDecimal;

public record FraudCheckRequest(

        Long transactionId,

        Long customerId,

        Long merchantId,

        BigDecimal amount,

        String currency

) {
}