package org.skybank.core.domain.service;

public interface AccountService {
    void deposit(int amount);
    void withdraw(int amount);
    void printStatement();

}
