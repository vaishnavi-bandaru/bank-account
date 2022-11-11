package com.example.bankaccount.controller;

import com.example.bankaccount.BankaccountApplication;
import com.example.bankaccount.controller.request.CustomerSignupRequest;
import com.example.bankaccount.repo.AccountRepository;
import com.example.bankaccount.repo.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BankaccountApplication.class)
@AutoConfigureMockMvc
@WithMockUser
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    public void before(){
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldBeAbleToSignupSuccessfully() throws Exception {
        String requestJson = objectMapper.writeValueAsString(new CustomerSignupRequest("abc", "abc@gmail.com", bCryptPasswordEncoder.encode("password")));

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());
    }
}
