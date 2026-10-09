package com.paystream.notification.service;

import com.paystream.notification.event.LedgerUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

	private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

	@Override
	public void sendTransactionNotification(LedgerUpdatedEvent event) {

		log.info("Payment notification generated. " + "Transaction: {}, Customer: {}, Amount: {} {}, Status: {}",
				event.transactionReference(), event.customerId(), event.amount(), event.currency(), event.status());
	}
}