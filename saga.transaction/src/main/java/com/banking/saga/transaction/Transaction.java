package com.banking.saga.transaction;

import java.time.LocalDateTime;
import java.util.Random;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private LocalDateTime timestamp;
    private double amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    
    private String senderAccountNum;

    private String receiverAccountNum;
    // Constructors, getters, and setters
    private String transactionNotes;
    
    public Transaction() {
    }

    public Transaction(double amount, String senderAccountNum, String receiverAccountNum) {
        this.transactionId = generateTransactionId();
        this.timestamp = LocalDateTime.now();
        this.amount = amount;
        this.senderAccountNum = senderAccountNum;
        this.receiverAccountNum = receiverAccountNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getSenderAccount() {
        return senderAccountNum;
    }

    public void setSenderAccount(String senderAccountNum) {
        this.senderAccountNum = senderAccountNum;
    }

    public String getReceiverAccount() {
        return receiverAccountNum;
    }

    public void setReceiverAccount(String receiverAccountNum) {
        this.receiverAccountNum = receiverAccountNum;
    }

    public String getTransactionNotes() {
        return transactionNotes;
    }

    public void setTransactionNotes(String transactionNotes) {
        this.transactionNotes = transactionNotes;
    } 
    public String generateTransactionId() {
        // Generate a 10-digit transactionId using a sequential approach
        int randomNumber = new Random().nextInt(900000000) + 100000000;
        return String.valueOf(randomNumber);
    }
}