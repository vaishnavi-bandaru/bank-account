package com.example.bankaccount.service;

import com.example.bankaccount.controller.request.CustomerSignupRequest;
import com.example.bankaccount.exceptions.AccountAlreadyExistsException;
import com.example.bankaccount.model.Customer;
import com.example.bankaccount.repo.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerPrincipalServiceTest {
    @Mock
    CustomerRepository customerRepository;
    @InjectMocks
    CustomerPrincipalService customerPrincipalService;

    @Test
    void shouldBeAbleToSaveCustomer() throws AccountAlreadyExistsException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        CustomerSignupRequest customerSignupRequest = new CustomerSignupRequest("abc", "abc@gmail.com", "password");
        Customer customer = new Customer("abc", "abc@gmail.com", bCryptPasswordEncoder.encode("password"));

        customerPrincipalService.save(customerSignupRequest, customer);

        verify(customerRepository).save(any());
    }

    @Test
    void shouldThrowAccountAlreadyExistsExceptionWhenEmailAlreadyLinkedToAnAccount() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        CustomerSignupRequest customerSignupRequest = new CustomerSignupRequest("abc", "abc@gmail.com", "password");
        Customer customer = new Customer("abc", "abc@gmail.com", bCryptPasswordEncoder.encode("password"));
        when(customerRepository.findByEmail("abc@gmail.com")).thenReturn(Optional.of(customer));

        assertThrows(AccountAlreadyExistsException.class, () -> customerPrincipalService.save(customerSignupRequest, customer));
    }
}
