package com.example.bankaccount.service;

import com.example.bankaccount.controller.response.StatementResponse;
import com.example.bankaccount.controller.response.TransactionResponse;
import com.example.bankaccount.model.Account;
import com.example.bankaccount.model.Customer;
import com.example.bankaccount.model.TRANSACTION_TYPE;
import com.example.bankaccount.model.Transaction;
import com.example.bankaccount.repo.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    AccountService accountService;
    @InjectMocks
    TransactionService transactionService;
    private Account account;
    private Customer customer;

    public void prepareData() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        customer = new Customer("abc", "abc@gmail.com", bCryptPasswordEncoder.encode("password"));
        account = new Account(new Date(), new BigDecimal(0), customer);
    }

    @Test
    void shouldBeAbleToSaveATransaction() {
        prepareData();

        transactionService.makeTransaction(account, TRANSACTION_TYPE.CREDIT, new BigDecimal(500));

        verify(transactionRepository).save(any());
        assertThat(account.getBalance(), is(new BigDecimal(500)));
    }

    @Test
    void shouldBeAbleToPerformATransaction() {
        prepareData();

        transactionService.makeTransaction(account, TRANSACTION_TYPE.CREDIT, new BigDecimal(1000));
        transactionService.makeTransaction(account, TRANSACTION_TYPE.DEBIT, new BigDecimal(500));

        assertThat(account.getBalance(), is(new BigDecimal(500)));
    }

    @Test
    void shouldBeAbleToGetTransactionsOfTheParticularUser() {
        prepareData();
        Transaction creditThousandRupees = new Transaction(new Date(), "CREDIT", new BigDecimal(1000), account);
        Transaction debitFiveHundredRupees = new Transaction(new Date(), "DEBIT", new BigDecimal(500), account);
        creditThousandRupees.setId(1);
        debitFiveHundredRupees.setId(2);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(creditThousandRupees);
        transactions.add(debitFiveHundredRupees);
        List<TransactionResponse> transactionResponses = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionResponse transactionResponse = TransactionResponse.builder().id(transaction.getId()).type(transaction.getTransaction_type()).amount(transaction.getTransaction_amount()).build();
            transactionResponses.add(transactionResponse);
        }
        when(accountService.getAccount("abc@gmail.com")).thenReturn(account);
        when(transactionRepository.findByAccountId(account.getId())).thenReturn(transactions);
        StatementResponse expectedStatementResponse = StatementResponse.builder().accountNumber(account.getId()).accountHolderName(account.getCustomer().getName()).transactions(transactionResponses).balance(account.getBalance()).build();

        StatementResponse statement = transactionService.statement("abc@gmail.com");

        assertThat(statement, is(expectedStatementResponse));
    }
}
