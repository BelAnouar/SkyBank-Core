package org.skybank.core.application.dto.response;

public record SignInResponse(
        boolean success,
        String message,
        AccountResponse account
) {}
