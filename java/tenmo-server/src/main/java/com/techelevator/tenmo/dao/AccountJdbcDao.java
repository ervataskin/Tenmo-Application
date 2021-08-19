package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class AccountJdbcDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public AccountJdbcDao(DataSource ds){

        this.jdbcTemplate = new JdbcTemplate(ds);}


    @Override
    public Account getBalance (String user){

        // given the String user, we need to figure out the account_id associated with that user
        // Another SQL query perhaps?
        // No longer have to hardcode the 2001 listed underneath.

        // 1. SQL query that retrieves an account_id given a username. Store this into a String.
        // 2. Run the query from step 1 using the jdbcTemplate, jdbcTemplate.queryForRowSet(....)
        // 3. Pull the account_id for that user out of the row set.
        // 4. you can plug this account_id into line 37 below, instead of 2001.




        Account account =new Account();
        String sql = "SELECT balance FROM accounts WHERE account_id=?";//query the database for specific users.
        SqlRowSet results =  jdbcTemplate.queryForRowSet(sql,2001);//This run the sql query.

        if(results.next()){ //scanning the results




            double balance =results.getDouble("balance");// we take out the value for the coloumns is called balance
            BigDecimal bigDecimal= new BigDecimal(balance);//transforming double into bigdecimal object.

            account.setBalance(bigDecimal); // puts in whatever retrieved from the database.

        }

        return account;
    }

    @Override
    public boolean checkBalance(String user, BigDecimal amount) {
        return false;
    }

    @Override
    public boolean verifyAccount(String user) {
        return false;
    }

    @Override
    public BigDecimal withdrawFunds() {
        return null;
    }

    @Override
    public BigDecimal depositFunds() {
        return null;
    }

}