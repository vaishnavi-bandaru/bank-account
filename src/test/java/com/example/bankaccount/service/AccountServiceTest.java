package com.example.bankaccount.service;

import com.example.bankaccount.controller.request.CustomerSignupRequest;
import com.example.bankaccount.controller.response.SummaryResponse;
import com.example.bankaccount.exceptions.AccountAlreadyExistsException;
import com.example.bankaccount.model.Account;
import com.example.bankaccount.model.Customer;
import com.example.bankaccount.repo.AccountRepository;
import com.example.bankaccount.repo.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class AccountServiceTest {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerPrincipalService customerPrincipalService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @BeforeEach
    public void beforeEach(){
        customerRepository = mock(CustomerRepository.class);
        accountRepository = mock(AccountRepository.class);
        customerPrincipalService = mock(CustomerPrincipalService.class);
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @AfterEach
    public void after(){
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldBeAbleToCreateAccountForCustomer() throws AccountAlreadyExistsException {
        AccountService accountService = new AccountService(accountRepository, customerPrincipalService);
        CustomerSignupRequest customerSignupRequest = new CustomerSignupRequest("abc", "abc@gmail.com", "password");

        accountService.save(customerSignupRequest);

        verify(accountRepository).save(any());
    }

    @Test
    void shouldBeAbleToReturnAccountWhenCustomerEmailIsGiven() {
        Customer customer = new Customer("abc", "abc@gmail.com", "password");
        customerRepository.save(customer);
        Account account = new Account(new Date(), new BigDecimal(0), customer);
        accountRepository.save(account);
        long customer_id = customer.getId();
        when(customerPrincipalService.getByEmail("abc@gmail.com")).thenReturn(customer);
        when(accountRepository.findByCustomer_Id(customer_id)).thenReturn(account);
        AccountService accountService = new AccountService(accountRepository, customerPrincipalService);

        Account fetchedAccount = accountService.getAccount(customer.getEmail());

        assertThat(fetchedAccount, is(account));
    }

    @Test
    void shouldBeAbleToFetchSummaryOfCustomer(){
        Customer customer = new Customer("abc", "abc@gmail.com", "password");
        customerRepository.save(customer);
        Account account = new Account(new Date(), new BigDecimal(100), customer);
        accountRepository.save(account);
        when(customerPrincipalService.getByEmail("abc@gmail.com")).thenReturn(customer);
        AccountService accountService = new AccountService(accountRepository,  customerPrincipalService);
        when(accountService.getAccount("abc@gmail.com")).thenReturn(account);
        SummaryResponse expectedSummary = new SummaryResponse(account.getId(), customer.getName(), account.getBalance());

        SummaryResponse actualSummary = accountService.summary("abc@gmail.com");

        assertThat(actualSummary, is(expectedSummary));
    }
}
