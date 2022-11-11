package com.example.bankaccount.controller;

import com.example.bankaccount.controller.request.CustomerSignupRequest;
import com.example.bankaccount.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/signup")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void signup(@RequestBody CustomerSignupRequest customerSignupRequest) throws IOException {
        accountService.save(customerSignupRequest);
    }
}
