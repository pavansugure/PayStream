package com.paystream.fraud.service;

import com.paystream.fraud.dto.FraudCheckRequest;
import com.paystream.fraud.dto.FraudCheckResponse;

public interface FraudService {

	FraudCheckResponse checkTransaction(FraudCheckRequest request);

}