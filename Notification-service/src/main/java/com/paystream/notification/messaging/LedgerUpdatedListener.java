package com.paystream.notification.messaging;

import com.paystream.notification.config.RabbitMQConfig;
import com.paystream.notification.event.LedgerUpdatedEvent;
import com.paystream.notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class LedgerUpdatedListener {

	private final NotificationService notificationService;

	public LedgerUpdatedListener(NotificationService notificationService) {

		this.notificationService = notificationService;
	}

	@RabbitListener(queues = RabbitMQConfig.LEDGER_UPDATED_QUEUE)
	public void handleLedgerUpdated(LedgerUpdatedEvent event) {

		notificationService.sendTransactionNotification(event);
	}
}