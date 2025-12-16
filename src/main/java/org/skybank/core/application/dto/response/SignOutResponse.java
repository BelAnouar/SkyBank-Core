package org.skybank.core.application.dto.response;

public record SignOutResponse(
        boolean success,
        String message
) {}
