package com.paystream.fraud.service;

import com.paystream.fraud.dto.FraudCheckRequest;
import com.paystream.fraud.dto.FraudCheckResponse;
import com.paystream.fraud.entity.FraudDecision;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class FraudServiceImpl implements FraudService {

	private static final BigDecimal MANUAL_REVIEW_THRESHOLD = new BigDecimal("100000");

	@Override
	public FraudCheckResponse checkTransaction(FraudCheckRequest request) {

		if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {

			return new FraudCheckResponse(request.transactionId(), FraudDecision.DECLINED,
					"Transaction amount must be greater than zero");
		}

		if (request.amount().compareTo(MANUAL_REVIEW_THRESHOLD) > 0) {

			return new FraudCheckResponse(request.transactionId(), FraudDecision.MANUAL_REVIEW,
					"Transaction amount exceeds configured review threshold");
		}

		return new FraudCheckResponse(request.transactionId(), FraudDecision.APPROVED,
				"Transaction passed fraud checks");
	}
}