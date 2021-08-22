package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
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
    public List<User> userList(Principal principal) {
        List<User> userlist = new ArrayList<>();
        List<User> userlist2 = userDao.findAll();

        for (User user : userlist2) {
            if (user.getUsername().equals(principal.getName())) {

            } else {
                userlist.add(user);
            }
        }
        return userlist;
    }

    @RequestMapping(path = "/user/{accountId}", method = RequestMethod.GET)
    public User findUsernameByAccountId(@PathVariable("accountId") Long accountId) {
        return userDao.findUsernameByAccountId(accountId);
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> getMyTransfers(Principal principal) {
        int userId = userDao.findIdByUsername(principal.getName());
        return transferDao.getMyTransfers((long) userId);
    }

    @RequestMapping (path = "/transfers/{transferId}", method = RequestMethod.GET)
    public Transfer getTransfer(@PathVariable("transferId") Long transferId) {
        return transferDao.getTransferById(transferId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfers/send", method = RequestMethod.POST)
    public Transfer sendTransfer(@RequestBody Transfer transfer, Principal principal) {
            int userId = userDao.findIdByUsername(principal.getName());
            Long accountFrom = accountDao.getAccountByUserId((long) userId).getAccount_id();

            Long accountTo = accountDao.getAccountByUserId(transfer.getAccount_to()).getAccount_id();

            return transferDao.sendTransfer(accountFrom, accountTo, transfer.getAmount());
        }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfers/request", method = RequestMethod.POST)
    public Transfer requestTransfer(@RequestBody Transfer transfer, Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        Long accountTo = accountDao.getAccountByUserId((long) userId).getAccount_id();

        Long accountFrom = accountDao.getAccountByUserId(transfer.getAccount_from()).getAccount_id();

        return transferDao.requestTransfer(accountFrom, accountTo, transfer.getAmount());
    }

    @RequestMapping (path = "/transfers/pending", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(Principal principal) {
        int userId = userDao.findIdByUsername(principal.getName());
        return transferDao.getPendingTransfers((long) userId);
    }

    @RequestMapping(path = "/transfers/approve", method = RequestMethod.PUT)
    public Transfer approveTransfer(@RequestBody Transfer transfer) {
        Long transferId = transfer.getTransfer_id();
     return transferDao.approveTransfer(transferId);
    }

    @RequestMapping(path = "/transfers/reject", method = RequestMethod.PUT)
    public Transfer rejectTransfer(@RequestBody Transfer transfer) {
        Long transferId = transfer.getTransfer_id();
        return transferDao.rejectTransfer(transferId);
    }
}
