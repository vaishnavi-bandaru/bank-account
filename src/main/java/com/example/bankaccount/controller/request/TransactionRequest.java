package com.example.bankaccount.controller.request;

import com.example.bankaccount.model.TRANSACTION_TYPE;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private TRANSACTION_TYPE type;
    private BigDecimal amount;
}
