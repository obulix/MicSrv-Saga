package com.banking.saga.transaction;

import java.time.LocalDateTime;

public class NotificationPOJO {

    private Long id;

    private String transactionId;
    private String accountId;
    private LocalDateTime currentDateTime;
    private String message;


    public NotificationPOJO() {
        this.currentDateTime = LocalDateTime.now();
    }

    public NotificationPOJO(String transactionId, String accountId, String message) {
        this();
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.message = message;
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(LocalDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}