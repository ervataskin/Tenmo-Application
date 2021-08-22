package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
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
    public List<User> userList(Principal principal) {
        List<User> userlist = userDao.findAll();
        List<User> userlist2 = userDao.findAll();

        for (User user : userlist2) {
            if (user.getUsername().equals(principal.getName())) {
                userlist.remove(user);
            }
        }

        return userlist;
    }

    @RequestMapping(path = "/send", method = RequestMethod.POST)
        public Transfer sendTransfer(@RequestBody Transfer transfer, Principal principal) {
            // find account id of current user, map to accountFrom.
            int userId = userDao.findIdByUsername(principal.getName());
            Long accountFrom = accountDao.getAccountByUserId((long) userId).getAccount_id();

            // convert userId to accountId for recipient, map to accountTo.
            Long accountTo = accountDao.getAccountByUserId(transfer.getAccount_to()).getAccount_id();

            //actually send the diddly dern transfer
            return transferDao.sendTransfer(accountFrom, accountTo, transfer.getAmount());
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
