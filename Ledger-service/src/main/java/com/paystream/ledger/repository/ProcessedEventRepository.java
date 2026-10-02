package com.paystream.ledger.repository;

import com.paystream.ledger.model.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, Long> {

	boolean existsByEventId(String eventId);
}