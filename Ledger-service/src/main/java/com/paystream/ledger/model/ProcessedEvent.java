package com.paystream.ledger.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "processed_events", uniqueConstraints = {
		@UniqueConstraint(name = "uk_processed_event_id", columnNames = "event_id") })
public class ProcessedEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "event_id", nullable = false, length = 100)
	private String eventId;

	@Column(name = "processed_at", nullable = false)
	private Instant processedAt;

	protected ProcessedEvent() {
	}

	public ProcessedEvent(String eventId, Instant processedAt) {

		this.eventId = eventId;
		this.processedAt = processedAt;
	}

	public Long getId() {
		return id;
	}

	public String getEventId() {
		return eventId;
	}

	public Instant getProcessedAt() {
		return processedAt;
	}
}