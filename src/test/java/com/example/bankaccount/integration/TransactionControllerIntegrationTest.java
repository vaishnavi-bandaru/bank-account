package com.example.bankaccount.integration;

import com.example.bankaccount.BankaccountApplication;
import com.example.bankaccount.controller.request.TransactionRequest;
import com.example.bankaccount.model.Account;
import com.example.bankaccount.model.Customer;
import com.example.bankaccount.model.TRANSACTION_TYPE;
import com.example.bankaccount.repo.AccountRepository;
import com.example.bankaccount.repo.CustomerRepository;
import com.example.bankaccount.repo.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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

import java.math.BigDecimal;
import java.util.Date;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BankaccountApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@WithMockUser
public class TransactionControllerIntegrationTest {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach(){
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @AfterEach()
    public void after(){
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldBeAbleToInvokeTransactionEndpoint() throws Exception {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Customer customer = new Customer("abc", "abc@gmail.com", bCryptPasswordEncoder.encode("password"));
        Customer savedCustomer = customerRepository.save(customer);
        Account account = new Account(new Date(), new BigDecimal(0), savedCustomer);
        accountRepository.save(account);
        String requestJson = objectMapper.writeValueAsString(new TransactionRequest(TRANSACTION_TYPE.CREDIT, new BigDecimal(500)));

        mockMvc.perform(post("/transaction")
                        .with(httpBasic("abc@gmail.com", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldBeAbleToGetTransactionHistoryOfAParticularUser() throws Exception {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Customer customer = new Customer("abc", "abc@gmail.com", bCryptPasswordEncoder.encode("password"));
        Customer savedCustomer = customerRepository.save(customer);
        Account account = new Account(new Date(), new BigDecimal(0), savedCustomer);
        accountRepository.save(account);

        mockMvc.perform(get("/transaction/statement")
                .with(httpBasic("abc@gmail.com", "password")))
                .andExpect(status().isOk());
    }
}
