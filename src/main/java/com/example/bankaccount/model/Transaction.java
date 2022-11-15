package com.example.bankaccount.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Date transaction_date;

    private String transaction_type;

    private BigDecimal transaction_amount;

    @OneToOne
    @JoinColumn(name = "account_number")
    private Account account;

    public Transaction(Date date, String type, BigDecimal amount, Account account) {
        this.transaction_date = date;
        this.transaction_type = type;
        this.transaction_amount = amount;
        this.account = account;
    }
}
