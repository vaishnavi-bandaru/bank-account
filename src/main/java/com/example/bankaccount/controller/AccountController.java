package com.example.bankaccount.controller;

import com.example.bankaccount.controller.request.CustomerSignupRequest;
import com.example.bankaccount.controller.response.SummaryResponse;
import com.example.bankaccount.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("account")
@AllArgsConstructor
public class AccountController {

    AccountService accountService;

    @PostMapping("/signup")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void signup(@RequestBody CustomerSignupRequest customerSignupRequest) throws IOException{
        accountService.save(customerSignupRequest);
    }

    @GetMapping("/summary")
    @ResponseStatus(code = HttpStatus.OK)
    public SummaryResponse getSummary(Principal principal){
        SummaryResponse summary = accountService.summary(principal.getName());
        return summary;
    }
}
