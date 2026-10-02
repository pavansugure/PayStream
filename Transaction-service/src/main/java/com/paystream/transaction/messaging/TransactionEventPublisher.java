package com.paystream.transaction.messaging;

import com.paystream.transaction.config.RabbitMQConfig;
import com.paystream.transaction.event.TransactionInitiatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventPublisher {

	private final RabbitTemplate rabbitTemplate;

	public TransactionEventPublisher(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void publish(TransactionInitiatedEvent event) {

		rabbitTemplate.convertAndSend(RabbitMQConfig.TRANSACTION_EXCHANGE,
				RabbitMQConfig.TRANSACTION_INITIATED_ROUTING_KEY, event);
	}
}