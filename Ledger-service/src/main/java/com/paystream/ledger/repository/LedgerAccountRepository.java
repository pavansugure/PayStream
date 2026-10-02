package com.paystream.ledger.repository;

import com.paystream.ledger.model.AccountType;
import com.paystream.ledger.model.LedgerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LedgerAccountRepository extends JpaRepository<LedgerAccount, Long> {

	Optional<LedgerAccount> findByOwnerIdAndAccountType(Long ownerId, AccountType accountType);
}