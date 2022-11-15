package com.example.bankaccount.exceptions;

public class AccountAlreadyExistsException extends Exception {
    public AccountAlreadyExistsException() {
        super("Account already exists");
    }
}
