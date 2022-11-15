package com.example.bankaccount.service;

import com.example.bankaccount.controller.response.StatementResponse;
import com.example.bankaccount.controller.response.TransactionResponse;
import com.example.bankaccount.model.Account;
import com.example.bankaccount.model.Customer;
import com.example.bankaccount.model.TRANSACTION_TYPE;
import com.example.bankaccount.model.Transaction;
import com.example.bankaccount.repo.AccountRepository;
import com.example.bankaccount.repo.CustomerRepository;
import com.example.bankaccount.repo.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class TransactionServiceTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @BeforeEach
    public void beforeEach(){
        transactionRepository = mock(TransactionRepository.class);
        customerRepository = mock(CustomerRepository.class);
        accountRepository = mock(AccountRepository.class);
        accountService = mock(AccountService.class);

    }

    @AfterEach()
    public void after(){
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldBeAbleToSaveATransaction() {
        TransactionService transactionService = new TransactionService(transactionRepository, accountService);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Customer customer = new Customer("abc", "abc@gmail.com", bCryptPasswordEncoder.encode("password"));
        Customer savedCustomer = customerRepository.save(customer);
        Account account = new Account(new Date(), new BigDecimal(0), savedCustomer);
        accountRepository.save(account);

        transactionService.makeTransaction(account, TRANSACTION_TYPE.CREDIT, new BigDecimal(500));

        verify(transactionRepository).save(any());
        assertThat(account.getBalance(), is(new BigDecimal(500)));
    }

    @Test
    void shouldBeAbleToPerformATransaction() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Customer customer = new Customer("abc", "abc@gmail.com", bCryptPasswordEncoder.encode("password"));
        Customer savedCustomer = customerRepository.save(customer);
        Account account = new Account(new Date(), new BigDecimal(0), savedCustomer);
        accountRepository.save(account);
        TransactionService transactionService = new TransactionService(transactionRepository, accountService);

        transactionService.makeTransaction(account, TRANSACTION_TYPE.CREDIT, new BigDecimal(1000));
        transactionService.makeTransaction(account, TRANSACTION_TYPE.DEBIT, new BigDecimal(500));

        assertThat(account.getBalance(), is(new BigDecimal(500)));
    }

    @Test
    void shouldBeAbleToGetTransactionsOfTheParticularUser() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Customer customer = new Customer("abc", "abc@gmail.com", bCryptPasswordEncoder.encode("password"));
        when(customerRepository.save(customer)).thenReturn(customer);
        Customer savedCustomer = customerRepository.save(customer);
        Account account = new Account(new Date(), new BigDecimal(0), savedCustomer);
        accountRepository.save(account);
        TransactionService transactionService = new TransactionService(transactionRepository, accountService);
        Transaction creditThousandRupees = new Transaction(new Date(), "CREDIT", new BigDecimal(1000), account);
        Transaction debitFiveHundredRupees = new Transaction(new Date(), "DEBIT", new BigDecimal(500), account);
        when(transactionRepository.save(creditThousandRupees)).thenReturn(creditThousandRupees);
        when(transactionRepository.save(debitFiveHundredRupees)).thenReturn(debitFiveHundredRupees);
        when(accountService.getAccount("abc@gmail.com")).thenReturn(account);
        transactionService.makeTransaction(account, TRANSACTION_TYPE.CREDIT, new BigDecimal(1000));
        transactionService.makeTransaction(account, TRANSACTION_TYPE.DEBIT, new BigDecimal(500));
        List<TransactionResponse> transactionResponses = new ArrayList<>();
        transactionResponses.add(new TransactionResponse(1L, "CREDIT", new BigDecimal(1000)));
        transactionResponses.add(new TransactionResponse(2L, "DEBIT", new BigDecimal(500)));
        StatementResponse expectedStatementResponse = new StatementResponse(account.getId(), account.getCustomer().getName(), transactionResponses, account.getBalance());

        StatementResponse statement = transactionService.statement("abc@gmail.com");

        assertThat(statement, is(expectedStatementResponse));
    }
}
