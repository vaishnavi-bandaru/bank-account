package com.example.bankaccount.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {
    @InjectMocks
    CustomerController customerController;
    @Mock
    Principal principal;

    @Test
    void shouldBeAbleToLoginSuccessfully() {
        String email = "abc@gmail.com";
        when(principal.getName()).thenReturn(email);
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("email", email);

        Map<String, Object> response = customerController.login(principal);

        assertThat(response, is(expectedResponse));
    }
}
