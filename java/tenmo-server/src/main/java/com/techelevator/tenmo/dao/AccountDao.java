package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;

public interface AccountDao {

    Account getBalance (String user);

    boolean checkBalance (String user, BigDecimal amount);

    boolean verifyAccount (String user);

    BigDecimal withdrawFunds();

    BigDecimal depositFunds();

}
