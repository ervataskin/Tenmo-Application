package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private BigDecimal balance;
    private Long account_id;
    private Long user_id;

    public Account(){
    }

    public Account(BigDecimal balance, Long account_id, Long user_id) {
        this.balance= balance;
        this.account_id = account_id;
        this.user_id = user_id;
    }


    public void setBalance(BigDecimal bigDecimal) {
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
