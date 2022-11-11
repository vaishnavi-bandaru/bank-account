package com.example.bankaccount.service;

import com.example.bankaccount.controller.request.CustomerSignupRequest;
import com.example.bankaccount.model.Account;
import com.example.bankaccount.model.Customer;
import com.example.bankaccount.repo.AccountRepository;
import com.example.bankaccount.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class AccountService {


    private CustomerRepository customerRepository;

    private AccountRepository accountRepository;

    private CustomerPrincipalService customerPrincipalService;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository, CustomerPrincipalService customerPrincipalService) {

        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.customerPrincipalService = customerPrincipalService;
    }

    public void save(CustomerSignupRequest customerSignupRequest) {
        Customer customer = new Customer(customerSignupRequest.getName(), customerSignupRequest.getEmail(), customerSignupRequest.getPassword());
        customerRepository.save(customer);
        Customer savedCustomer = customerPrincipalService.getByEmail(customerSignupRequest.getEmail());
        Account account = new Account(new Date(), new BigDecimal(0), savedCustomer);
        accountRepository.save(account);
    }
}
