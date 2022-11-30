package com.example.bankaccount.controller;

import com.example.bankaccount.controller.request.TransactionRequest;
import com.example.bankaccount.controller.response.StatementResponse;
import com.example.bankaccount.model.Account;
import com.example.bankaccount.service.AccountService;
import com.example.bankaccount.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@AllArgsConstructor
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    AccountService accountService;
    TransactionService transactionService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void transaction(Principal principal, @RequestBody TransactionRequest transactionRequest) {
        Account account = accountService.getAccount(principal.getName());
        transactionService.makeTransaction(account, transactionRequest.getType(), transactionRequest.getAmount());
    }

    @GetMapping("/statement")
    @ResponseStatus(code = HttpStatus.OK)
    public StatementResponse getStatement(Principal principal) {
        return transactionService.statement(principal.getName());
    }
}
