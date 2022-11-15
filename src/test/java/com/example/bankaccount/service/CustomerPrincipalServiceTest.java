package com.example.bankaccount.service;

import com.example.bankaccount.controller.request.CustomerSignupRequest;
import com.example.bankaccount.exceptions.AccountAlreadyExistsException;
import com.example.bankaccount.model.Customer;
import com.example.bankaccount.repo.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerPrincipalServiceTest {
    @Autowired
    CustomerRepository customerRepository;

    @BeforeEach
    void before() {
        customerRepository = mock(CustomerRepository.class);
    }

    @AfterEach
    public void after() {
        customerRepository.deleteAll();
    }

    @Test
    void shouldBeAbleToSaveCustomer() throws AccountAlreadyExistsException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        CustomerSignupRequest customerSignupRequest = new CustomerSignupRequest("abc", "abc@gmail.com", "password");
        Customer customer = new Customer("abc", "abc@gmail.com", bCryptPasswordEncoder.encode("password"));
        CustomerPrincipalService customerPrincipalService = new CustomerPrincipalService(customerRepository);

        customerPrincipalService.save(customerSignupRequest, customer);

        verify(customerRepository).save(any());
    }

    @Test
    void shouldThrowAccountAlreadyExistsExceptionWhenEmailAlreadyLinkedToAnAccount() { BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        CustomerSignupRequest customerSignupRequest = new CustomerSignupRequest("abc", "abc@gmail.com", "password");
        Customer customer = new Customer("abc", "abc@gmail.com", bCryptPasswordEncoder.encode("password"));
        when(customerRepository.findByEmail("abc@gmail.com")).thenReturn(Optional.of(customer));

        CustomerPrincipalService customerPrincipalService = new CustomerPrincipalService(customerRepository);

        assertThrows(AccountAlreadyExistsException.class, ()-> customerPrincipalService.save(customerSignupRequest,customer));
    }
}
