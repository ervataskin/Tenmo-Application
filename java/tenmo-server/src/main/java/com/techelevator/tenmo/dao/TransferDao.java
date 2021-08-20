package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    List<Transfer> getMyTransfers(String user);

    Transfer getTransferById(Long id);

    Long createTransfer(Long transferType, Long accountFrom, Long accountTo, BigDecimal Amount);

    Transfer requestTransfer(Long accountFrom, Long accountTo, BigDecimal amount);

    Transfer sendTransfer(Long accountFrom, Long accountTo, BigDecimal amount);

    Transfer approveTransfer(Long transferId);

    Transfer rejectTransfer(Long transferId);

}
