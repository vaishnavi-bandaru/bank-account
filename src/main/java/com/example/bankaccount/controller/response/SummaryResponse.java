package com.example.bankaccount.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Builder
@Getter
@Setter
public class SummaryResponse {
    private Long accountNumber;
    private String accountHolderName;
    private BigDecimal balance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SummaryResponse that = (SummaryResponse) o;
        return accountNumber.equals(that.accountNumber) && accountHolderName.equals(that.accountHolderName) && balance.equals(that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, accountHolderName, balance);
    }
}
