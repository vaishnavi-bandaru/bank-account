package com.example.bankaccount.service;

import com.example.bankaccount.config.security.SecurityConfig;
import com.example.bankaccount.controller.request.CustomerSignupRequest;
import com.example.bankaccount.controller.response.SummaryResponse;
import com.example.bankaccount.model.Account;
import com.example.bankaccount.model.Customer;
import com.example.bankaccount.repo.AccountRepository;
import com.example.bankaccount.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class AccountService {


    private CustomerRepository customerRepository;

    private AccountRepository accountRepository;

    private CustomerPrincipalService customerPrincipalService;

    private SecurityConfig securityConfig;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository, CustomerPrincipalService customerPrincipalService) {

        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.customerPrincipalService = customerPrincipalService;
        securityConfig = new SecurityConfig(customerPrincipalService);
        passwordEncoder = securityConfig.getPasswordEncoder();
    }

    public void save(CustomerSignupRequest customerSignupRequest){

        String password = passwordEncoder.encode(customerSignupRequest.getPassword());
        Customer customer = new Customer(customerSignupRequest.getName(), customerSignupRequest.getEmail(), password);
        customerPrincipalService.save(customer);

        Customer savedCustomer = customerPrincipalService.getByEmail(customerSignupRequest.getEmail());
        Account account = new Account(new Date(), new BigDecimal(0), savedCustomer);
        accountRepository.save(account);

    }

    public Account getAccount(String email){
        Customer customer = customerPrincipalService.getByEmail(email);
        long customer_id = customer.getId();
        Account account = accountRepository.findByCustomer_Id(customer_id);
        return account;
    }

    public SummaryResponse summary(String email) {
        Account account = getAccount(email);
        SummaryResponse summary = new SummaryResponse(account.getId(), account.getCustomer().getName(), account.getBalance());
        return summary;
    }
}
