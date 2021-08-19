package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.security.Principal;

@Component
public class AccountJdbcDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    UserDao userDao;

    public AccountJdbcDao(DataSource ds){

        this.jdbcTemplate = new JdbcTemplate(ds);}


    @Override
    public BigDecimal getAccountBalance (String user){
        //TODO: create an overloaded method of getbalance that allows us to use the userId OR the username;
        int user_id = userDao.findIdByUsername(user);
        Account account = new Account();
        String sql = "SELECT balance FROM accounts WHERE user_id=?";//query the database for specific users.
        SqlRowSet results =  jdbcTemplate.queryForRowSet(sql,user_id);//This run the sql query.

        if(results.next()){ //scanning the results
            double balance = results.getDouble("balance");// we take out the value for the coloumns is called balance
            BigDecimal bigDecimal= new BigDecimal(balance);//transforming double into bigdecimal object.

            account.setBalance(bigDecimal); // puts in whatever retrieved from the database.
        }
        return account.getBalance();
    }

    @Override
    public boolean checkBalance(String user, BigDecimal amount) {
        //TODO: Change to use userId
        BigDecimal currentBalance = getAccountBalance(user);

        boolean enoughFunds = false;

        if (currentBalance.compareTo(amount) >= 0) {
            enoughFunds = true;
        }
        return enoughFunds;
    }

    @Override
    public boolean verifyAccount(String user) {
        //TODO: Change to use userId
        int userId = userDao.findIdByUsername(user);
        String sql = "SELECT account_id FROM accounts WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public BigDecimal withdrawFunds(String user, BigDecimal amount) {
        //TODO: Change to use the userId instead of the username
        boolean checkBalance = checkBalance(user, amount);
        BigDecimal newBalance;

        if (checkBalance) {
            int userId = userDao.findIdByUsername(user);
            newBalance = getAccountBalance(user).subtract(amount);
            String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?;";
            jdbcTemplate.update(sql, newBalance, userId);
        } else {
            newBalance = getAccountBalance(user);
        }
        return newBalance;
    }

    @Override
    public BigDecimal depositFunds(String user, BigDecimal amount) {
        //TODO: Change this to use the userId instead of the user directly.
        BigDecimal newBalance = getAccountBalance(user).add(amount);
        int userId = userDao.findIdByUsername(user);
        String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?;";
        jdbcTemplate.update(sql, newBalance, userId);
        return newBalance;
    }

}