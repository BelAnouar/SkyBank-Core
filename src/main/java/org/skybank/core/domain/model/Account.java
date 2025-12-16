package org.skybank.core.domain.model;


import lombok.*;


import java.util.Date;
import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private  String accountNumber;
    private int balance;
    private  List<Transaction> transactions;

    public void deposit(int amount, Date date) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount;
        transactions.add(new Transaction(date, amount, balance));
    }

    public void withdraw(int amount, Date date) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance -= amount;
        transactions.add(new Transaction(date, -amount, balance));
    }


}