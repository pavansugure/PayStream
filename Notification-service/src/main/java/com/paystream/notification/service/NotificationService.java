package com.paystream.notification.service;

import com.paystream.notification.event.LedgerUpdatedEvent;

public interface NotificationService {

	void sendTransactionNotification(LedgerUpdatedEvent event);
}