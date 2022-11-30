package com.example.bankaccount.integration;

import com.example.bankaccount.BankaccountApplication;
import com.example.bankaccount.model.Customer;
import com.example.bankaccount.repo.AccountRepository;
import com.example.bankaccount.repo.CustomerRepository;
import com.example.bankaccount.repo.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BankaccountApplication.class)
@WithMockUser
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void before() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        customerRepository.deleteAll();
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void shouldInvokeCustomerEndpoint() throws Exception {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode("password");
        String requestJSON = objectMapper.writeValueAsString(new Customer("abc", "abc@gmail.com", password));

        mockMvc.perform(get("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(requestJSON)).andExpect(status().isOk());
    }

    @Test
    void shouldShowUnauthorizedWhenUserDoesNotHaveAccount() throws Exception {
        mockMvc.perform(get("/login")
                        .with(httpBasic("abc@gmail.com", bCryptPasswordEncoder.encode("password"))))
                .andExpect(status().isUnauthorized());
    }
}
