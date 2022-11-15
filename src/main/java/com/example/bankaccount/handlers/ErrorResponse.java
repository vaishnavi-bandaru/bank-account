package com.example.bankaccount.handlers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ErrorResponse {
    @JsonProperty
    private final String errorMessage;

    @JsonProperty
    private final List<String> details;

    public ErrorResponse(String errorMessage, List<String> details) {
        this.errorMessage = errorMessage;
        this.details = details;
    }
}
