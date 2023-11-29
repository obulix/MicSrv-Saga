package com.banking.saga.account;

import java.time.LocalDateTime;
import java.util.Random;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TransactionPOJO {

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

    private String transactionNotes;
    
    public TransactionPOJO() {
    }

    public TransactionPOJO(Long id,String transactionId,LocalDateTime timestamp, double amount, String senderAccountNum, String receiverAccountNum, String transactionNotes) {
    	this.id = id;
    	this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.amount = amount;
        this.senderAccountNum = senderAccountNum;
        this.receiverAccountNum = receiverAccountNum;
        this.transactionNotes =transactionNotes;
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
    
}