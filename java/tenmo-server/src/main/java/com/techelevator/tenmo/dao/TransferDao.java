package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    Transfer getTransferById(Long transferId);

    List<Transfer> getMyTransfers(Long userId);

    List<Transfer> getPendingTransfers(Long userId);

    Long createTransfer (Long transferType, Long accountFrom, Long accountTo, BigDecimal amount);

    Transfer sendTransfer(Long accountFrom, Long accountTo, BigDecimal amount);

    Transfer requestTransfer(Long accountFrom, Long accountTo, BigDecimal amount);

    Transfer approveTransfer(Long transferId);

    Transfer rejectTransfer(Long transferId);

    void updateTransferStatus(Long transferId, Long statusId);
}
