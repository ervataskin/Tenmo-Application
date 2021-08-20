package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    List<Transfer> getMyTransfers(String user);

    Transfer getTransferById(Long id);

    Long createTransfer(Long transferType, Long transferStatus, Long accountFrom, Long accountTo, BigDecimal Amount);

    void requestTransfer(Long accountFrom, Long accountTo, BigDecimal amount);

    void sendTransfer(Long accountFrom, Long accountTo, BigDecimal amount);

    void approveTransfer(Long transferId);

}
