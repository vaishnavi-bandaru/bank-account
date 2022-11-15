package com.example.bankaccount.model;

import java.math.BigDecimal;

public enum TRANSACTION_TYPE {
    CREDIT(new BigDecimal(1)), DEBIT(new BigDecimal(-1));

    private BigDecimal multiplicationFactor;

    TRANSACTION_TYPE(BigDecimal multiplicationFactor) {
        this.multiplicationFactor = multiplicationFactor;
    }

    public BigDecimal getMultiplicationFactor() {
        return multiplicationFactor;
    }
}
