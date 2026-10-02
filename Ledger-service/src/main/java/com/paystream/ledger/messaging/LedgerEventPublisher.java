package com.paystream.ledger.messaging;

import com.paystream.ledger.config.RabbitMQConfig;
import com.paystream.ledger.event.LedgerUpdatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class LedgerEventPublisher {

	private final RabbitTemplate rabbitTemplate;

	public LedgerEventPublisher(RabbitTemplate rabbitTemplate) {

		this.rabbitTemplate = rabbitTemplate;
	}

	public void publish(LedgerUpdatedEvent event) {

		rabbitTemplate.convertAndSend(RabbitMQConfig.LEDGER_EXCHANGE, RabbitMQConfig.LEDGER_UPDATED_ROUTING_KEY, event);
	}
}