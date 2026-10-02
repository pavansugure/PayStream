package com.paystream.ledger.service;

import com.paystream.ledger.event.FraudCheckCompletedEvent;

public interface LedgerService {

	void processFraudCheckCompleted(FraudCheckCompletedEvent event);
}