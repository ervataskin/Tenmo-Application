package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class TransferJdbcDao implements TransferDao {

    @Autowired


    @Override
    public List<Transfer> getMyTransfers() {
        return null;
    }

    @Override
    public Transfer getTransferById(Long id) {
        // sql statement for getting transfer by id
        // map row to transfer object
        return null;
    }

    @Override
    public Long createTransfer(Long transferType, Long transferStatus, Long accountFrom, Long accountTo, BigDecimal amount) {
        //create object and then pass that into the sql statement
        //sql statement to insert transfer into transfer table;
        //transfer should include all information
        //you should be able to pass a status into this method from the method that calls it
        //it should return the transfer id so that the transfer can be manipulated within the next method,
        //so this method should return a Long
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

        Long id = createTransfer(transferType, transferStatus, accountFrom, accountTo, amount);

    }

    @Override
    public void approveTransfer(Long transferId) {
        Transfer thisTransfer = getTransferById(transferId);

    }


}
