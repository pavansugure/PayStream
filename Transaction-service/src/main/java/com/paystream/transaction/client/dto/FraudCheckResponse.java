package com.paystream.transaction.client.dto;

public record FraudCheckResponse(

		Long transactionId,

		String decision,

		String reason

) {
}