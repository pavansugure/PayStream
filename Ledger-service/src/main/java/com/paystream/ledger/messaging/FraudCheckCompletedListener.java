package com.paystream.ledger.messaging;

import com.paystream.ledger.config.RabbitMQConfig;
import com.paystream.ledger.event.FraudCheckCompletedEvent;
import com.paystream.ledger.service.LedgerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FraudCheckCompletedListener {

	private final LedgerService ledgerService;

	public FraudCheckCompletedListener(LedgerService ledgerService) {

		this.ledgerService = ledgerService;
	}

	@RabbitListener(queues = RabbitMQConfig.FRAUD_CHECK_COMPLETED_QUEUE)
	public void handleFraudCheckCompleted(FraudCheckCompletedEvent event) {

		ledgerService.processFraudCheckCompleted(event);
	}
}