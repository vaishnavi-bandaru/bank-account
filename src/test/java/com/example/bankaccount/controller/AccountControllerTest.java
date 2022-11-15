package com.example.bankaccount.controller;

import com.example.bankaccount.BankaccountApplication;
import com.example.bankaccount.controller.request.CustomerSignupRequest;
import com.example.bankaccount.repo.AccountRepository;
import com.example.bankaccount.repo.CustomerRepository;
import com.example.bankaccount.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = BankaccountApplication.class)
@AutoConfigureMockMvc
@WithMockUser
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AccountControllerTest {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountService accountService;

    @BeforeEach
    public void before(){
        accountRepository.deleteAll();
        customerRepository.deleteAll();
        accountService = mock(AccountService.class);
    }

    @Test
    void shouldBeAbleToSignupSuccessfullyWhenDetailsAreGiven() throws Exception {
        CustomerSignupRequest customerSignupRequest = new CustomerSignupRequest("abc", "abc@gmail.com", "password");
        AccountController accountController = new AccountController(accountService);

        accountController.signup(customerSignupRequest);

        verify(accountService).save(customerSignupRequest);
    }

    @Test
    void shouldBeAbleToGetSummaryOfAUser() {
        AccountController accountController = new AccountController(accountService);
        Principal principal = mock(Principal.class);
        String email = "abc@gmail.com";
        when(principal.getName()).thenReturn(email);

        accountController.getSummary(principal);

        verify(accountService).summary(email);
    }
}
