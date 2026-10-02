package com.paystream.fraud.dto;

import com.paystream.fraud.entity.FraudDecision;

public record FraudCheckResponse(

        Long transactionId,

        FraudDecision decision,

        String reason

) {
}