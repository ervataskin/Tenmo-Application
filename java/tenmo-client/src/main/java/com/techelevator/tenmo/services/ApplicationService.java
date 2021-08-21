package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ApplicationService {

    private RestTemplate restTemplate = new RestTemplate();
    private String BASE_URL;
    public static String AUTH_TOKEN = "";

    public ApplicationService(String url) {
        this.BASE_URL = url;
    }

    public Account getMyBalance(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Account> response = restTemplate.exchange(BASE_URL + "balance", HttpMethod.GET, entity, Account.class);
        return response.getBody();
    }

//    public List<User> Allusers (String token){
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        HttpEntity entity = new HttpEntity(headers);
//        ResponseEntity<List<User>>response = restTemplate.exchange(BASE_URL + "users", HttpMethod.GET, entity, User.class);
//        return response.getBody();
//
//    }
    public Transfer sendTransfer (String token,long transferId){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + "send", HttpMethod.POST, entity, Transfer.class);
        return response.getBody();
    }

    public Transfer requestTransfer (String token,Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + "request", HttpMethod.POST, entity, Transfer.class);
        return response.getBody();
    }
   // public List<Transfer> getMyTransfers (String token, Transfer transfer){


    
    public Transfer approveTransfer (String token, Long transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + "transfers/approve", HttpMethod.PUT, entity, Transfer.class);
        return response.getBody();


    }


    public Transfer rejectTransfer (String token,Long transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + "transfers/reject", HttpMethod.PUT, entity, Transfer.class);
        return response.getBody();


    }
}