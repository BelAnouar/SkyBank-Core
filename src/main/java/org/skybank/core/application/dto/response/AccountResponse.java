package org.skybank.core.application.dto.response;



public record AccountResponse(
        String accountNumber,
        int balance,
        int transactionCount
) {}