package org.skybank.core.domain.service.implemantation;


import org.skybank.core.application.dto.request.SignInRequest;

import org.skybank.core.application.dto.response.CreateAccountResponse;
import org.skybank.core.application.dto.response.SignInResponse;
import org.skybank.core.application.dto.response.SignOutResponse;
import org.skybank.core.application.mapper.AuthMapper;
import org.skybank.core.domain.context.AccountContext;
import org.skybank.core.domain.exception.AuthenticationException;
import org.skybank.core.domain.model.Account;
import org.skybank.core.domain.service.AuthService;

import java.util.ArrayList;
import java.util.UUID;

public class AuthServiceImpl implements AuthService {

        private final AuthMapper authMapper = AuthMapper.INSTANCE;

        @Override
        public CreateAccountResponse createAccount() {
            try {
                String accountNumber = generateAccountNumber();

                Account newAccount = new Account();
                newAccount.setAccountNumber(accountNumber);
                newAccount.setBalance(0);
                newAccount.setTransactions(new ArrayList<>());


                AccountContext.setCurrentAccount(newAccount);


                return authMapper.toCreateAccountResponse(newAccount);

            } catch (Exception e) {
                return authMapper.toCreateAccountErrorResponse("Failed to create account: " + e.getMessage());
            }
        }

        @Override
        public SignInResponse signIn(SignInRequest request) {
            try {

                if (request == null) {
                    throw new AuthenticationException("Sign in request cannot be null");
                }


                Account account = new Account();
                account.setAccountNumber(request.accountNumber());
                account.setBalance(0);
                account.setTransactions(new ArrayList<>());


                if (account.getAccountNumber() == null || account.getAccountNumber().trim().isEmpty()) {
                    throw new AuthenticationException("Invalid account: Account number is required");
                }

                AccountContext.setCurrentAccount(account);


                return authMapper.toSignInResponse(account);

            } catch (AuthenticationException e) {
                return authMapper.toSignInErrorResponse("Sign in failed: " + e.getMessage());
            } catch (Exception e) {
                return authMapper.toSignInErrorResponse("Unexpected error: " + e.getMessage());
            }
        }

        @Override
        public SignOutResponse signOut() {
            try {
                if (AccountContext.getCurrentAccount() == null) {
                    throw new AuthenticationException("No user is currently signed in");
                }

                AccountContext.clear();

                return new SignOutResponse(true, "Signed out successfully");

            } catch (AuthenticationException e) {
                return new SignOutResponse(false, "Sign out failed: " + e.getMessage());
            } catch (Exception e) {
                return new SignOutResponse(false, "Unexpected error: " + e.getMessage());
            }
        }

        @Override
        public Account getCurrentAccount() {
            return AccountContext.getCurrentAccount();
        }

        private String generateAccountNumber() {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            return "ACC" + uuid.substring(0, 9).toUpperCase();
        }
    }