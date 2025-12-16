package org.skybank.core;


import org.skybank.core.domain.service.AccountService;
import org.skybank.core.domain.service.AuthService;
import org.skybank.core.domain.service.implemantation.AccountServiceImpl;
import org.skybank.core.domain.service.implemantation.AuthServiceImpl;
import org.skybank.core.presentation.ConsolePresenter;

public class App 
{
    public static void main(String[] args) {
        AuthService authService = new AuthServiceImpl();
        AccountService accountService = new AccountServiceImpl();
        ConsolePresenter consolePresenter = new ConsolePresenter(authService, accountService);

        consolePresenter.start();
    }
}

