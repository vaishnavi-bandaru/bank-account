package com.example.bankaccount.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
public class StatementResponse {
    private Long accountNumber;
    private String accountHolderName;
    private List<TransactionResponse> transactions;
    private BigDecimal balance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatementResponse that = (StatementResponse) o;
        return Objects.equals(accountNumber, that.accountNumber) && Objects.equals(accountHolderName, that.accountHolderName) && Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, accountHolderName, balance);
    }
}
