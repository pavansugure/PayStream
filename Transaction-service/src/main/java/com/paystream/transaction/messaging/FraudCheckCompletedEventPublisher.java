package com.paystream.transaction.messaging;

import com.paystream.transaction.config.RabbitMQConfig;
import com.paystream.transaction.event.FraudCheckCompletedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class FraudCheckCompletedEventPublisher {

	private final RabbitTemplate rabbitTemplate;

	public FraudCheckCompletedEventPublisher(RabbitTemplate rabbitTemplate) {

		this.rabbitTemplate = rabbitTemplate;
	}

	public void publish(FraudCheckCompletedEvent event) {

		rabbitTemplate.convertAndSend(RabbitMQConfig.TRANSACTION_EXCHANGE,
				RabbitMQConfig.FRAUD_CHECK_COMPLETED_ROUTING_KEY, event);
	}
}