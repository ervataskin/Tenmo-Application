package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransferJdbcDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    AccountDao accountDao;

    @Autowired
    UserDao userDao;

    public TransferJdbcDao(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public List<Transfer> getMyTransfers(String user) {
        List<Transfer> myTransfers = new ArrayList<>();
        int userId = userDao.findIdByUsername(user);
        String sql = "SELECT transfer_id FROM transfers WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            Long transferId = results.getLong("transfer_id");
            myTransfers.add(getTransferById(transferId));
        }
        return myTransfers;
        //TODO: Update toString method for all models (client side?)
    }

    @Override
    public Transfer getTransferById(Long id) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount" +
                "FROM transfers WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public Long createTransfer(Long transferType, Long accountFrom, Long accountTo, BigDecimal amount) {
        Long transferStatus = 1L;
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING transfer_id;";
        Long newId = jdbcTemplate.queryForObject(sql, Long.class, transferType, transferStatus, accountFrom, accountTo, amount);
        return newId;
    }

    @Override
    public Transfer requestTransfer(Long accountFrom, Long accountTo, BigDecimal amount) {
        Long transferType = 1L;
        Long id = createTransfer(transferType, accountFrom, accountTo, amount);
        return getTransferById(id);
    }

    @Override
    public Transfer sendTransfer(Long accountFrom, Long accountTo, BigDecimal amount) {
        Long transferType = 2L;

        Long id = createTransfer(transferType, accountFrom, accountTo, amount);
        Transfer thisTransfer = approveTransfer(id);

        return thisTransfer;
    }

    @Override
    public Transfer approveTransfer(Long transferId) {
        Transfer thisTransfer = getTransferById(transferId);
            //TODO: this will actually directly call the userId for both accounts, use the userId directly in those methods.
        if (thisTransfer.getId() == 1) { // check that transfer status == 1
            if (accountDao.checkBalance()) {// is accountFrom balance higher than amount being transferred
                // do the math
                String sql = "UPDATE transfers SET transfer_status_id = 2 WHERE transfer_id = ?;";
                jdbcTemplate.update(sql, transferId);

            } else {
                //return error that there isn't enough money in the account, don't change transfer status.
            }
        } else {
            //return error that transfer cannot be approved because it has already been approved or rejected
        }
    }

    public Transfer rejectTransfer (Long transferId) {
        Transfer thisTransfer = getTransferById(transferId);
        thisTransfer.setStatusId(3L);
        String sql = "UPDATE transfers SET transfer_status_id = 3 WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, thisTransfer.getId());
        return thisTransfer;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setId(results.getLong("transfer_id"));
        transfer.setTypeId(results.getLong("transfer_type_id"));
        transfer.setStatusId(results.getLong("transfer_status_id"));
        transfer.setAccountFrom(results.getLong("account_from"));
        transfer.setAccountTo(results.getLong("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }
    /* Hi Erva! This is what I've managed to stub out so far.
    * There are like 3 different places where I'm considering putting the verify account and check balance methods from the accountDao.
    * I think they should probably go into the createTransfer method, because it's silly to create a transfer in the database if it can't be completed.
    * Would love to know your thoughts on this when/if you get a chance!
    *
    * Other things I did for this DAO:
    * - set up a mapRowToTransfer method so we only had to worry about that once
    * - set up getTransferById so we can turn any transfer that exists in the database into a transfer object and manipulate the data from there
    *  */


}
