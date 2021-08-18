package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class AccountJdbcDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public AccountJdbcDao(DataSource ds){

        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public Account getAccount (User user){
        Account retrievedAccount = null;
        String sql = "SELECT balance, account_id, user_id FROM accounts WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user.getId());
        if (results.next()) {
            mapRowToAccount(results);
        }
        //why is this always null? what have i done to anger the java spirits?
        return retrievedAccount;
    }

    @Override
    public void withdraw(BigDecimal withdrawAmt) {

    }

    @Override
    public void deposit(BigDecimal depositAmount) {

    }

    @Override
    public Account createAccount(Long userId) {
        return null;
    }

    private Account mapRowToAccount (SqlRowSet results) {
        //translates sql rowset results into a java account object that we can use to manipulate data before updating
        BigDecimal balance = results.getBigDecimal("balance");
        Long account_id = results.getLong("account_id");
        Long user_id = results.getLong("user_id");
        Account newAccount = new Account(balance, account_id, user_id);
        return newAccount;
    }
}
