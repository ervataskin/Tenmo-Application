package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    Account getMyBalance(String username);

    Account getAccountById(Long accountId);

    Account getAccountByUserId(Long userId);

    Account getAccountByUsername(String username);

    boolean checkAccountBalance(Long accountId, BigDecimal amount);

    void withdraw(Long accountId, BigDecimal fromAmount);

    void deposit(Long accountId, BigDecimal toAmount);

    void updateAccount(Long accountId, BigDecimal newBalance);
}