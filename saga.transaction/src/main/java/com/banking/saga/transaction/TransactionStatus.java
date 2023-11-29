package com.banking.saga.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TransactionStatus {
    @JsonProperty("0")
    PENDING,
    @JsonProperty("1")
    SUCCESS,
    @JsonProperty("2")
    FAILURE
}