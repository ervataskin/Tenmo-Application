package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
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
public class TransferJdbcDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;

    @Autowired
    AccountDao accountDao;

    public TransferJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getTransferById(Long transferId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer.setTransfer_id(results.getLong("transfer_id"));
            transfer.setTransfer_type_id(results.getLong("transfer_type_id"));
            transfer.setTransfer_status_id(results.getLong("transfer_status_id"));
            transfer.setAccount_to(results.getLong("account_to"));
            transfer.setAccount_from(results.getLong("account_from"));
            transfer.setAmount(results.getBigDecimal("amount"));
        }
        return transfer;
    }

    @Override
    public List<Transfer> getMyTransfers(Long userId) {
        List<Transfer> myTransfers = new ArrayList<>();

        Account account = accountDao.getAccountByUserId(userId);
        Long accountId = account.getAccount_id();

        String sql = "SELECT transfer_id FROM transfers WHERE account_from = ? OR account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);

        while (results.next()) {
            Long transferId = results.getLong("transfer_id");
            myTransfers.add(getTransferById(transferId));
        }

        return myTransfers;
    }

    @Override
    public List<Transfer> getPendingTransfers(Long userId) {
        List<Transfer> pendingTransfers = new ArrayList<>();

        Account account = accountDao.getAccountByUserId(userId);
        Long accountId = account.getAccount_id();

        String sql = "SELECT transfer_id FROM transfers WHERE account_from = ? OR account_to = ? AND transfer_status_id = 1;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);

        while (results.next()) {
            Long transferId = results.getLong("transfer_id");
            pendingTransfers.add(getTransferById(transferId));
        }
        return pendingTransfers;
    }

    @Override
    public Long createTransfer(Long transferType, Long accountFrom, Long accountTo, BigDecimal amount) {
        Long transferStatus = 1L;
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        Long newId = jdbcTemplate.queryForObject(sql, Long.class, transferType, transferStatus, accountFrom, accountTo, amount);
        return newId;
    }

    @Override
    public Transfer sendTransfer(Long accountFrom, Long accountTo, BigDecimal amount) {
        Long transferType = 2L;
        Long id = createTransfer(transferType, accountFrom, accountTo, amount);

        Transfer transfer = approveTransfer(id);

        return transfer;
    }

    @Override
    public Transfer requestTransfer(Long accountFrom, Long accountTo, BigDecimal amount) {
        Long transferType = 1L;

        Long id = createTransfer(transferType, accountFrom, accountTo, amount);
        return getTransferById(id);
    }

    @Override
    public Transfer approveTransfer(Long transferId) {
        Transfer transfer = getTransferById(transferId);
        Long toId = transfer.getAccount_to();
        Long fromId = transfer.getAccount_from();
        BigDecimal transferAmount = transfer.getAmount();

        boolean fundsCheck = accountDao.checkAccountBalance(fromId, transferAmount);

        if (fundsCheck) {
            Long statusId = 2L;
            accountDao.withdraw(fromId, transferAmount);
            accountDao.deposit(toId, transferAmount);
            updateTransferStatus(transferId, statusId);
        } else {
            rejectTransfer(transferId);
        }

        return getTransferById(transferId);
    }

    @Override
    public Transfer rejectTransfer(Long transferId) {
        Long statusId = 3L;
        updateTransferStatus(transferId, statusId);
        return getTransferById(transferId);
    }

    @Override
    public void updateTransferStatus(Long transferId, Long statusId) {
        String sql = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, statusId, transferId);
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransfer_id(results.getLong("transfer_id"));
        transfer.setTransfer_type_id(results.getLong("transfer_type_id"));
        transfer.setTransfer_status_id(results.getLong("transfer_status_id"));
        transfer.setAccount_from(results.getLong("account_from"));
        transfer.setAccount_to(results.getLong("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }
}