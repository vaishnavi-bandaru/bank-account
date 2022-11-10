package com.example.bankaccount.controller;

import com.example.bankaccount.BankaccountApplication;
import com.example.bankaccount.model.Customer;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = BankaccountApplication.class)
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CustomerRepository customerRepository;


    @BeforeEach
    public void before(){
        customerRepository.deleteAll();
    }

    @AfterEach
    public void after(){
        customerRepository.deleteAll();
    }

    @Test
    void shouldBeAbleToLoginSuccessfully() throws Exception {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Customer customer = new Customer("abc", "abc@gmail.com", bCryptPasswordEncoder.encode("password"));
        customerRepository.save(customer);

        mockMvc.perform(get("/login")
                .with(httpBasic("abc@gmail.com", "password")))
                .andExpect(status().isOk());
    }
}
