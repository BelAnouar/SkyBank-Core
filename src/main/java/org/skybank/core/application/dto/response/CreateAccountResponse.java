package org.skybank.core.application.dto.response;

public record CreateAccountResponse(
        boolean success,
        String message,
        String accountNumber,
        int balance
) {}
