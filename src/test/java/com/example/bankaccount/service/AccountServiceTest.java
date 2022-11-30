package com.example.bankaccount.service;

import com.example.bankaccount.controller.request.CustomerSignupRequest;
import com.example.bankaccount.controller.response.SummaryResponse;
import com.example.bankaccount.exceptions.AccountAlreadyExistsException;
import com.example.bankaccount.model.Account;
import com.example.bankaccount.model.Customer;
import com.example.bankaccount.repo.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CustomerPrincipalService customerPrincipalService;
    @InjectMocks
    AccountService accountService;

    @Test
    void shouldBeAbleToCreateAccountForCustomer() throws AccountAlreadyExistsException {
        String email = "abc@gmail.com";
        CustomerSignupRequest customerSignupRequest = new CustomerSignupRequest("abc", email, "password");
        Customer customer = new Customer();
        when(customerPrincipalService.getByEmail(email)).thenReturn(customer);

        accountService.save(customerSignupRequest);

        verify(accountRepository).save(any());
    }

    @Test
    void shouldBeAbleToReturnAccountWhenCustomerEmailIsGiven() {
        Customer customer = new Customer("abc", "abc@gmail.com", "password");
        Account account = new Account(new Date(), new BigDecimal(0), customer);
        accountRepository.save(account);
        long customer_id = customer.getId();
        when(customerPrincipalService.getByEmail("abc@gmail.com")).thenReturn(customer);
        when(accountRepository.findByCustomerId(customer_id)).thenReturn(account);

        Account fetchedAccount = accountService.getAccount(customer.getEmail());

        assertThat(fetchedAccount, is(account));
    }

    @Test
    void shouldBeAbleToFetchSummaryOfCustomer() {
        Customer customer = new Customer("abc", "abc@gmail.com", "password");
        Account account = new Account(new Date(), new BigDecimal(100), customer);
        accountRepository.save(account);
        when(customerPrincipalService.getByEmail("abc@gmail.com")).thenReturn(customer);
        when(accountService.getAccount("abc@gmail.com")).thenReturn(account);
        SummaryResponse expectedSummary = SummaryResponse.builder().accountNumber(account.getId()).accountHolderName(customer.getName()).balance(account.getBalance()).build();

        SummaryResponse actualSummary = accountService.summary("abc@gmail.com");

        assertThat(actualSummary, is(expectedSummary));
    }
}
