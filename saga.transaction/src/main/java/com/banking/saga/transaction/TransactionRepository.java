package com.banking.saga.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	   Transaction findBytransactionId(String transactionId);
}