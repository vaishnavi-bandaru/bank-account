package com.example.bankaccount.controller;

import com.example.bankaccount.BankaccountApplication;
import com.example.bankaccount.repo.AccountRepository;
import com.example.bankaccount.repo.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BankaccountApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    public void before(){
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @AfterEach
    public void after(){
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldBeAbleToLoginSuccessfully(){
        String email = "abc@gmail.com";
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(email);
        CustomerController customerController = new CustomerController();
        Map<String,Object> expectedResponse = new HashMap<>();
        expectedResponse.put("email", email);

        Map<String, Object> response = customerController.login(principal);

        assertThat(response, is(expectedResponse));
    }

}
