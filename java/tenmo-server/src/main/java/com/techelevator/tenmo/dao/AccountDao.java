package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;

public interface AccountDao {

    Account getAccount (User user);

    //maybe make these not void later
    void withdraw(BigDecimal withdrawAmt);

    void deposit(BigDecimal depositAmount);

    Account createAccount (Long userId);

}
