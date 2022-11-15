package com.example.bankaccount.controller;

import com.example.bankaccount.controller.request.TransactionRequest;
import com.example.bankaccount.model.Account;
import com.example.bankaccount.model.Customer;
import com.example.bankaccount.model.TRANSACTION_TYPE;
import com.example.bankaccount.repo.AccountRepository;
import com.example.bankaccount.repo.CustomerRepository;
import com.example.bankaccount.service.AccountService;
import com.example.bankaccount.service.TransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @BeforeEach
    public void before(){
        transactionService = mock(TransactionService.class);
        accountService = mock(AccountService.class);
        customerRepository = mock(CustomerRepository.class);
        accountRepository = mock(AccountRepository.class);
    }

    @AfterEach
    public void after(){
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldBeAbleToCreditAmountInAccountOfAUser() {
        TransactionController transactionController = new TransactionController(accountService, transactionService);
        Principal principal = mock(Principal.class);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Customer customer = new Customer("abc", "abc@gmail.com", bCryptPasswordEncoder.encode("password"));
        Customer savedCustomer = customerRepository.save(customer);
        Account account = new Account(new Date(), new BigDecimal(0), savedCustomer);
        accountRepository.save(account);
        when(principal.getName()).thenReturn("abc@gmail.com");
        when(accountService.getAccount("abc@gmail.com")).thenReturn(account);
        TransactionRequest transactionRequest = new TransactionRequest(TRANSACTION_TYPE.CREDIT, new BigDecimal(1000));

        transactionController.transaction(principal, transactionRequest);

        verify(transactionService).makeTransaction(account, TRANSACTION_TYPE.CREDIT, new BigDecimal(1000));
    }

    @Test
    void shouldBeAbleToGetStatementOfAUser() {
        String email = "abc@gmail.com";
        TransactionController transactionController = new TransactionController(accountService, transactionService);
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(email);

        transactionController.getStatement(principal);

        verify(transactionService).statement(email);
    }
}
