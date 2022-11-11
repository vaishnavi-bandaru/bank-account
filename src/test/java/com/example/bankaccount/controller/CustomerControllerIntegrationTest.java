package com.example.bankaccount.controller;

import com.example.bankaccount.BankaccountApplication;
import com.example.bankaccount.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.http.HttpResponse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(classes = BankaccountApplication.class)
@WithMockUser
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldInvokeCustomerEndpoint() throws Exception {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode("password");
        String requestJSON = objectMapper.writeValueAsString(new Customer("abc", "abc@gmail.com", password));

        MvcResult mvcResult = mockMvc.perform(get("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(requestJSON)
        ).andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(HttpStatus.OK.value()));
        assertThat(mvcResult.getResponse().getContentAsString(), is("{\"email\":\"user\"}"));
    }
}
