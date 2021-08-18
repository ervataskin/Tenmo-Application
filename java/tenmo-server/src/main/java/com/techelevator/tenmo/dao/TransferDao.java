package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    List<Transfer> getMyTransfers();

    Transfer getTransferById();

    void requestTransfer();

    void sendTransfer();

}
