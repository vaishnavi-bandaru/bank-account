package com.example.bankaccount.service;

import com.example.bankaccount.controller.request.CustomerSignupRequest;
import com.example.bankaccount.model.Customer;
import com.example.bankaccount.repo.AccountRepository;
import com.example.bankaccount.repo.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

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

    @Test
    void shouldBeAbleToCreateAccountForCustomer() {
        Customer customer = new Customer("abc", "abc@gmail.com", "password");
        AccountService accountService = new AccountService(accountRepository, customerRepository, customerPrincipalService);
        CustomerSignupRequest customerSignupRequest = new CustomerSignupRequest("abc", "abc@gmail.com", "password");
        when(customerRepository.findByEmail("abc@gmail.com")).thenReturn(Optional.of(customer));

        accountService.save(customerSignupRequest);

        verify(accountRepository).save(any());
    }

    @Test
    void shouldBeAbleToSaveCustomer() {
        Customer customer = new Customer("abc", "abc@gmail.com", "password");
        AccountService accountService = new AccountService(accountRepository, customerRepository, customerPrincipalService);
        CustomerSignupRequest customerSignupRequest = new CustomerSignupRequest("abc", "abc@gmail.com", "password");
        when(customerRepository.findByEmail("abc@gmail.com")).thenReturn(Optional.of(customer));

        accountService.save(customerSignupRequest);

        verify(customerRepository).save(any());
    }
}
