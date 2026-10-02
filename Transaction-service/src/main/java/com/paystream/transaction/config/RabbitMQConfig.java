package com.paystream.transaction.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

@Configuration
public class RabbitMQConfig {

	public static final String TRANSACTION_EXCHANGE = "paystream.transaction.exchange";

	public static final String TRANSACTION_INITIATED_QUEUE = "transaction.initiated.queue";

	public static final String TRANSACTION_INITIATED_ROUTING_KEY = "transaction.initiated";

	public static final String FRAUD_CHECK_COMPLETED_QUEUE = "fraud.check.completed.queue";

	public static final String FRAUD_CHECK_COMPLETED_ROUTING_KEY = "fraud.check.completed";

	@Bean
	public DirectExchange transactionExchange() {
		return new DirectExchange(TRANSACTION_EXCHANGE, true, false);
	}

	@Bean
	public Queue transactionInitiatedQueue() {
		return new Queue(TRANSACTION_INITIATED_QUEUE, true);
	}

	@Bean
	public Binding transactionInitiatedBinding(Queue transactionInitiatedQueue, DirectExchange transactionExchange) {

		return BindingBuilder.bind(transactionInitiatedQueue).to(transactionExchange)
				.with(TRANSACTION_INITIATED_ROUTING_KEY);
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
	public JacksonJsonMessageConverter jackson2JsonMessageConverter() {
		return new JacksonJsonMessageConverter();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
			JacksonJsonMessageConverter messageConverter) {

		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter);

		return rabbitTemplate;
	}

	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}

}