package com.example.bankaccount.controller;

import com.example.bankaccount.BankaccountApplication;
import com.example.bankaccount.model.Customer;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = BankaccountApplication.class)
@AutoConfigureMockMvc
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AccountRepository accountRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void before(){
        accountRepository.deleteAll();
        customerRepository.deleteAll();
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @AfterEach
    public void after(){
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldBeAbleToLoginSuccessfully() throws Exception {
        Customer customer = new Customer("abc", "abc@gmail.com", bCryptPasswordEncoder.encode("password"));
        customerRepository.save(customer);

        mockMvc.perform(get("/login")
                .with(httpBasic("abc@gmail.com", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("abc@gmail.com"));
    }

    @Test
    void shouldShowUnauthorizedWhenUserDoesNotHaveAccount() throws Exception {

        mockMvc.perform(get("/login")
                        .with(httpBasic("abc@gmail.com", bCryptPasswordEncoder.encode("password"))))
                .andExpect(status().isUnauthorized());
    }
}
