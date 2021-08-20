package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@PreAuthorize("isAuthenticated()")
@RestController
public class ApplicationController {

     @Autowired
     private AccountDao accountDao;
     @Autowired
     private TransferDao transferDao;

     @RequestMapping(path = "/balance", method = RequestMethod.GET)
     public Account getMyBalance(Principal principal) {
          return accountDao.getMyBalance(principal.getName());
     }

}