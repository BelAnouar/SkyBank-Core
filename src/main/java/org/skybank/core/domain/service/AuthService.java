package org.skybank.core.domain.service;

import org.skybank.core.application.dto.request.SignInRequest;

import org.skybank.core.application.dto.response.CreateAccountResponse;
import org.skybank.core.application.dto.response.SignInResponse;
import org.skybank.core.application.dto.response.SignOutResponse;
import org.skybank.core.domain.model.Account;

public interface AuthService {
    CreateAccountResponse createAccount();
    SignInResponse signIn(SignInRequest request);
    SignOutResponse signOut();
    Account getCurrentAccount();
}