package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("isAuthenticated")
public class ApplicationController {


     @Autowired
     AccountDao dao;

   //  @RequestMapping (path= "/balance",method = RequestMethod.GET,httpEntity);


}
