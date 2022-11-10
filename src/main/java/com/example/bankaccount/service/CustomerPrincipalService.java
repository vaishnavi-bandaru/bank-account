package com.example.bankaccount.service;

import com.example.bankaccount.model.Customer;
import com.example.bankaccount.model.CustomerPrincipal;
import com.example.bankaccount.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerPrincipalService implements UserDetailsService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = getByEmail(email);
        return new CustomerPrincipal(customer);
    }

    private Customer getByEmail(String email) {
        return customerRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("user not found"));
    }
}
