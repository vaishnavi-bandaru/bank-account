package com.example.bankaccount.service;

import com.example.bankaccount.controller.response.StatementResponse;
import com.example.bankaccount.controller.response.TransactionResponse;
import com.example.bankaccount.model.Account;
import com.example.bankaccount.model.TRANSACTION_TYPE;
import com.example.bankaccount.model.Transaction;
import com.example.bankaccount.repo.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class TransactionService {


    @Autowired
    TransactionRepository transactionRepository;


    @Autowired
    AccountService accountService;

    @Transactional
    public void makeTransaction(Account account, TRANSACTION_TYPE type, BigDecimal amount) {
        Transaction transaction = new Transaction(new Date(), type.toString(), amount, account);
        transactionRepository.save(transaction);
        BigDecimal currentBalance = account.getBalance();
        BigDecimal transactionAmount = amount.multiply(type.getMultiplicationFactor());
        account.setBalance(new BigDecimal(String.valueOf(currentBalance.add(transactionAmount))));
    }

    public StatementResponse statement(String email) {
        Account account = accountService.getAccount(email);
        long accountNumber = account.getId();
        List<Transaction> transactions = transactionRepository.findByAccountId(accountNumber);
        List<TransactionResponse> transactionResponse = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionResponse.add(new TransactionResponse(transaction.getId(), transaction.getTransaction_type(), transaction.getTransaction_amount()));
        }
        return new StatementResponse(accountNumber, account.getCustomer().getName(), transactionResponse, account.getBalance());
    }
}
