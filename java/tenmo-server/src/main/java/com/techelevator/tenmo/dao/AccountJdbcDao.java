package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class AccountJdbcDao implements AccountDao{
    private JdbcTemplate jdbcTemplate;

    @Autowired
    UserDao userDao;

    public AccountJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getMyBalance(String username) {
        int userId = userDao.findIdByUsername(username);
        Account account = new Account();
        String sql = "SELECT balance FROM accounts WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            account.setBalance(results.getBigDecimal("balance"));
        }
        return account;
    }

    @Override
    public Account getAccountById(Long accountId) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public Account getAccountByUserId(Long userId) {
        Account account = new Account();
        String sql = "SELECT account_id FROM accounts WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            account.setAccount_id(results.getLong("account_id"));
        }
        return account;
    }

    @Override
    public Account getAccountByUsername(String username) {
        int userId = userDao.findIdByUsername(username);
        return getAccountByUserId((long) userId);
    }

    @Override
    public boolean checkAccountBalance(Long accountId, BigDecimal amount) {
        Account account = getAccountById(accountId);
        if (account.getBalance().compareTo(amount) >= 0) {
            return true;
        }
        return false;
    }

    @Override
    public void withdraw(Long accountId, BigDecimal fromAmount) {
        BigDecimal fromBalance = getAccountById(accountId).getBalance();
        BigDecimal newBalance = fromBalance.subtract(fromAmount);
        updateAccount(accountId, newBalance);
    }

    @Override
    public void deposit(Long accountId, BigDecimal toAmount) {
        BigDecimal toBalance = getAccountById(accountId).getBalance();
        BigDecimal newBalance = toBalance.add(toAmount);
        updateAccount(accountId, newBalance);
    }

    @Override
    public void updateAccount(Long acctId, BigDecimal newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, newBalance, acctId);
    }

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        account.setAccount_id(results.getLong("account_id"));
        account.setBalance(results.getBigDecimal("balance"));
        account.setUser_id(results.getLong("user_id"));
        return account;
    }

}