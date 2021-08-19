package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

@Component
public class TransferJdbcDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    AccountDao accountDao;

    public TransferJdbcDao(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public List<Transfer> getMyTransfers() {
        return null;
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
    public Long createTransfer(Long transferType, Long transferStatus, Long accountFrom, Long accountTo, BigDecimal amount) {
        //create object and then pass that into the sql statement
        //sql statement to insert transfer into transfer table;
        //transfer should include all information
        //you should be able to pass a status into this method from the method that calls it
        //it should return the transfer id so that the transfer can be manipulated within the next method,
        //so this method should return a Long or int. Figure out which of these we need.
        Long newId = ;
        return newId;

    }

    @Override
    public void requestTransfer(Long accountFrom, Long accountTo, BigDecimal amount) {
        Long transferType = 1L;
        Long transferStatus = 1L;

        Long id = createTransfer(transferType, transferStatus, accountFrom, accountTo, amount);

    }

    @Override
    public void sendTransfer(Long accountFrom, Long accountTo, BigDecimal amount) {
        Long transferType = 2L;
        Long transferStatus = 2L;

        //creates transfer row in transfer table.
        // Actually maybe this is where we need to check the balance, and then again on approval for requested transfers.
        Long id = createTransfer(transferType, transferStatus, accountFrom, accountTo, amount);

        //create transfer object to manipulate
        // check balance using accountDAO method
            // if balance > amount, approve transfer, update balances.
            // if balance < amount, reject transfer, return error.
                //TODO: set up "reject transfer" method;

    }

    @Override
    public void approveTransfer(Long transferId) {
        Transfer thisTransfer = getTransferById(transferId);
        //get "to" account, "from" account, and amount to transfer.
            //TODO: this will actually directly call the userId for both accounts, use the userId directly in those methods.
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
