package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("isAuthenticated()")
public class ApplicationController {


     @Autowired
     AccountDao accountDao;

     @Autowired
     TransferDao transferDao;

     @Autowired
     UserDao userDao;

   //  @RequestMapping (path= "/balance", method = RequestMethod.GET,httpEntity);

   //  @RequestMapping (path= "/transfers", method = RequestMethod.GET,httpEntity);

   //  @RequestMapping (path= "/transfers/send", method = RequestMethod.POST,httpEntity);

   //  @RequestMapping (path= "/transfers/request", method = RequestMethod.POST,httpEntity);

   //  @RequestMapping (path= "/transfers/approve", method = RequestMethod.PUT,httpEntity);


}
