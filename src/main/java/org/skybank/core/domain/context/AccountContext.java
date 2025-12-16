package org.skybank.core.domain.context;

import org.skybank.core.domain.model.Account;

public class AccountContext {
    private static final ThreadLocal<Account> currentAccount = new ThreadLocal<>();

    public static void setCurrentAccount(Account account) {
        currentAccount.set(account);
    }

    public static Account getCurrentAccount() {
        return currentAccount.get();
    }

    public static void clear() {
        currentAccount.remove();
    }
}