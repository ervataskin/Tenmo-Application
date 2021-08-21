package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class ApplicationController {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TransferDao transferDao;
    @Autowired
    private UserDao userDao;

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public Account getMyBalance(Principal principal) {
        return accountDao.getMyBalance(principal.getName());
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> listAllUsers(Principal principal) {
        return userDao.findAll();
    }

    @RequestMapping(path = "/send", method = RequestMethod.POST)
    public Transfer sendTransfer(Long accountFrom, Long accountTo, BigDecimal amount) {
    return transferDao.sendTransfer(accountFrom, accountTo, amount);
    }

    @RequestMapping(path = "/request", method = RequestMethod.POST)
    public Transfer requestTransfer(Long accountFrom, Long accountTo, BigDecimal amount){
     return transferDao.requestTransfer(accountFrom, accountTo,amount);
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> getMyTransfers(Principal principal) {
         int userId = userDao.findIdByUsername(principal.getName());
         return transferDao.getMyTransfers((long) userId);

    }

    @RequestMapping(path = "/transfers/approve", method = RequestMethod.PUT)
    public Transfer approveTransfer(Long transferId) {
     return transferDao.approveTransfer(transferId);
    }

    @RequestMapping(path = "/transfers/reject", method = RequestMethod.PUT)
    public Transfer rejectTransfer(Long transferId) {
    return transferDao.rejectTransfer(transferId);
    }
}
