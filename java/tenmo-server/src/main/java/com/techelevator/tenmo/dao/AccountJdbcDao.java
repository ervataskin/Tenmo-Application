package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
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

        Account balance =new Account();

        balance.setBalance (new BigDecimal("1000"));

        return balance;
}
}
