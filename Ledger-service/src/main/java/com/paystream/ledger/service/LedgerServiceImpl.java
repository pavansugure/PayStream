package com.paystream.ledger.service;

import com.paystream.ledger.event.FraudCheckCompletedEvent;
import com.paystream.ledger.model.AccountType;
import com.paystream.ledger.model.LedgerAccount;
import com.paystream.ledger.model.LedgerEntry;
import com.paystream.ledger.model.LedgerEntryType;
import com.paystream.ledger.model.ProcessedEvent;
import com.paystream.ledger.repository.LedgerAccountRepository;
import com.paystream.ledger.repository.LedgerEntryRepository;
import com.paystream.ledger.repository.ProcessedEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.paystream.ledger.event.LedgerUpdatedEvent;
import com.paystream.ledger.messaging.LedgerEventPublisher;

import java.math.BigDecimal;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LedgerServiceImpl implements LedgerService {

	private static final Logger log = LoggerFactory.getLogger(LedgerServiceImpl.class);
	private final LedgerAccountRepository ledgerAccountRepository;
	private final LedgerEntryRepository ledgerEntryRepository;
	private final ProcessedEventRepository processedEventRepository;
	private final LedgerEventPublisher ledgerEventPublisher;

	public LedgerServiceImpl(LedgerAccountRepository ledgerAccountRepository,
			LedgerEntryRepository ledgerEntryRepository, ProcessedEventRepository processedEventRepository,
			LedgerEventPublisher ledgerEventPublisher) {

		this.ledgerAccountRepository = ledgerAccountRepository;
		this.ledgerEntryRepository = ledgerEntryRepository;
		this.processedEventRepository = processedEventRepository;
		this.ledgerEventPublisher = ledgerEventPublisher;
	}

	@Override
	@Transactional
	public void processFraudCheckCompleted(FraudCheckCompletedEvent event) {

		/*
		 * Ledger only processes approved transactions.
		 *
		 * DECLINED and MANUAL_REVIEW transactions do not create accounting entries.
		 */
		if (!"APPROVED".equals(event.fraudDecision())) {

			log.info("Skipping ledger processing for transaction {} because fraud decision is {}",
					event.transactionReference(), event.fraudDecision());

			return;
		}

		/*
		 * RabbitMQ can redeliver a message.
		 *
		 * If this event was already processed, we simply ignore the duplicate.
		 */
		if (processedEventRepository.existsByEventId(event.eventId())) {

			log.info("Ignoring duplicate ledger event {} for transaction {}", event.eventId(),
					event.transactionReference());

			return;
		}

		Instant now = Instant.now();

		LedgerAccount customerAccount = getOrCreateAccount(event.customerId(), AccountType.CUSTOMER, event.currency(),
				now);

		LedgerAccount merchantAccount = getOrCreateAccount(event.merchantId(), AccountType.MERCHANT, event.currency(),
				now);

		BigDecimal amount = event.amount();

		/*
		 * Customer pays the merchant.
		 *
		 * Customer account → DEBIT Merchant account → CREDIT
		 */
		customerAccount.setBalance(customerAccount.getBalance().subtract(amount));

		customerAccount.setUpdatedAt(now);

		merchantAccount.setBalance(merchantAccount.getBalance().add(amount));

		merchantAccount.setUpdatedAt(now);

		ledgerAccountRepository.save(customerAccount);
		ledgerAccountRepository.save(merchantAccount);

		/*
		 * Create the corresponding double-entry records.
		 */
		LedgerEntry customerDebit = new LedgerEntry(event.transactionReference(), event.customerId(),
				LedgerEntryType.DEBIT, amount, event.currency(), now);

		LedgerEntry merchantCredit = new LedgerEntry(event.transactionReference(), event.merchantId(),
				LedgerEntryType.CREDIT, amount, event.currency(), now);

		ledgerEntryRepository.save(customerDebit);
		ledgerEntryRepository.save(merchantCredit);

		/*
		 * Mark the event as processed only after the accounting entries have been
		 * created.
		 */
		processedEventRepository.save(new ProcessedEvent(event.eventId(), now));

		LedgerUpdatedEvent ledgerUpdatedEvent = new LedgerUpdatedEvent(event.eventId(), event.transactionReference(),
				event.customerId(), event.merchantId(), event.amount(), event.currency(), "COMPLETED", now);

		ledgerEventPublisher.publish(ledgerUpdatedEvent);

		log.info("Ledger updated successfully for transaction {}. Amount: {} {}", event.transactionReference(),
				event.amount(), event.currency());
	}

	private LedgerAccount getOrCreateAccount(Long ownerId, AccountType accountType, String currency, Instant now) {

		return ledgerAccountRepository.findByOwnerIdAndAccountType(ownerId, accountType)
				.orElseGet(() -> ledgerAccountRepository
						.save(new LedgerAccount(ownerId, accountType, BigDecimal.ZERO, currency, now, now)));
	}
}