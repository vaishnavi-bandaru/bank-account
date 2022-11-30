package com.example.bankaccount.controller;

import com.example.bankaccount.controller.request.CustomerSignupRequest;
import com.example.bankaccount.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
    @Mock
    AccountService accountService;
    @InjectMocks
    AccountController accountController;

    @Test
    void shouldBeAbleToSignupSuccessfullyWhenDetailsAreGiven() throws Exception {
        CustomerSignupRequest customerSignupRequest = new CustomerSignupRequest("abc", "abc@gmail.com", "password");

        accountController.signup(customerSignupRequest);

        verify(accountService).save(customerSignupRequest);
    }

    @Test
    void shouldBeAbleToGetSummaryOfAUser() {
        Principal principal = mock(Principal.class);
        String email = "abc@gmail.com";
        when(principal.getName()).thenReturn(email);

        accountController.getSummary(principal);

        verify(accountService).summary(email);
    }
}
