package org.skybank.core.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignInRequest(
        @NotBlank(message = "Account number is required")
        @Pattern(regexp = "^ACC[A-Z0-9]{9}$", message = "Invalid account number format")
        String accountNumber
) {}
