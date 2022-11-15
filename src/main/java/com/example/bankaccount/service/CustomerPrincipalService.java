package com.example.bankaccount.service;

import com.example.bankaccount.controller.request.CustomerSignupRequest;
import com.example.bankaccount.exceptions.AccountAlreadyExistsException;
import com.example.bankaccount.model.Customer;
import com.example.bankaccount.model.CustomerPrincipal;
import com.example.bankaccount.repo.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerPrincipalService implements UserDetailsService {

    CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = getByEmail(email);
        return new CustomerPrincipal(customer);
    }

    public Customer getByEmail(String email) {
        return customerRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    public void save(CustomerSignupRequest customerSignupRequest, Customer customer) throws AccountAlreadyExistsException {
        Optional<Customer> existingCustomer = customerRepository.findByEmail(customerSignupRequest.getEmail());
        if (existingCustomer.isPresent()) throw new AccountAlreadyExistsException();
        customerRepository.save(customer);
    }
}
