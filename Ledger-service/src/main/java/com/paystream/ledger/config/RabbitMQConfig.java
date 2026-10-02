package com.paystream.ledger.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

	public static final String TRANSACTION_EXCHANGE = "paystream.transaction.exchange";

	public static final String FRAUD_CHECK_COMPLETED_QUEUE = "fraud.check.completed.queue";

	public static final String FRAUD_CHECK_COMPLETED_ROUTING_KEY = "fraud.check.completed";

	public static final String LEDGER_EXCHANGE = "paystream.ledger.exchange";

	public static final String LEDGER_UPDATED_QUEUE = "ledger.updated.queue";

	public static final String LEDGER_UPDATED_ROUTING_KEY = "ledger.updated";

	@Bean
	public DirectExchange transactionExchange() {

		return new DirectExchange(TRANSACTION_EXCHANGE, true, false);
	}

	@Bean
	public Queue fraudCheckCompletedQueue() {

		return new Queue(FRAUD_CHECK_COMPLETED_QUEUE, true);
	}

	@Bean
	public Binding fraudCheckCompletedBinding(Queue fraudCheckCompletedQueue, DirectExchange transactionExchange) {

		return BindingBuilder.bind(fraudCheckCompletedQueue).to(transactionExchange)
				.with(FRAUD_CHECK_COMPLETED_ROUTING_KEY);
	}

	@Bean
	public DirectExchange ledgerExchange() {

		return new DirectExchange(LEDGER_EXCHANGE, true, false);
	}

	@Bean
	public Queue ledgerUpdatedQueue() {

		return new Queue(LEDGER_UPDATED_QUEUE, true);
	}

	@Bean
	public Binding ledgerUpdatedBinding(Queue ledgerUpdatedQueue, DirectExchange ledgerExchange) {

		return BindingBuilder.bind(ledgerUpdatedQueue).to(ledgerExchange).with(LEDGER_UPDATED_ROUTING_KEY);
	}

	@Bean
	public JacksonJsonMessageConverter messageConverter() {

		return new JacksonJsonMessageConverter();
	}
}