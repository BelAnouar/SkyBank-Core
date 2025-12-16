package org.skybank.core.domain.model;


import lombok.*;

import java.util.Date;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private  Date date;
    private  int amount;
    private  int balance;

}