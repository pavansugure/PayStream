package com.paystream.transaction.service;

import com.paystream.transaction.dto.CreateTransactionRequest;
import com.paystream.transaction.dto.TransactionResponse;
import org.springframework.security.core.Authentication;

public interface TransactionService {

	TransactionResponse createTransaction(CreateTransactionRequest request, Authentication authentication);

	TransactionResponse getTransaction(String transactionReference);
}