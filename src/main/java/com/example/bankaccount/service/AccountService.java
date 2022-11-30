package com.example.bankaccount.service;

import com.example.bankaccount.controller.request.CustomerSignupRequest;
import com.example.bankaccount.controller.response.SummaryResponse;
import com.example.bankaccount.exceptions.AccountAlreadyExistsException;
import com.example.bankaccount.model.Account;
import com.example.bankaccount.model.Customer;
import com.example.bankaccount.repo.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@Service
public class AccountService {
    private AccountRepository accountRepository;
    private CustomerPrincipalService customerPrincipalService;

    public void save(CustomerSignupRequest customerSignupRequest) throws AccountAlreadyExistsException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode(customerSignupRequest.getPassword());
        Customer customer = new Customer(customerSignupRequest.getName(), customerSignupRequest.getEmail(), password);
        customerPrincipalService.save(customerSignupRequest, customer);

        Customer savedCustomer = customerPrincipalService.getByEmail(customerSignupRequest.getEmail());
        Account account = new Account(new Date(), new BigDecimal(0), savedCustomer);
        accountRepository.save(account);
    }

    public Account getAccount(String email) {
        Customer customer = customerPrincipalService.getByEmail(email);
        long customer_id = customer.getId();
        Account account = accountRepository.findByCustomerId(customer_id);
        return account;
    }

    public SummaryResponse summary(String email) {
        Account account = getAccount(email);
        SummaryResponse summary = SummaryResponse.builder().accountNumber(account.getId()).accountHolderName(account.getCustomer().getName()).balance(account.getBalance()).build();
        return summary;
    }
}
