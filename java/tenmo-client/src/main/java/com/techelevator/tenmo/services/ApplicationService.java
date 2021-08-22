package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientResponseException;
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

    public User[] getAllUsers(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<User[]> response =
                restTemplate.exchange(BASE_URL + "users", HttpMethod.GET, entity, User[].class);
        return response.getBody();
    }

    public Transfer[] getMyTransfers(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Transfer[]> response =
                restTemplate.exchange(BASE_URL + "transfers", HttpMethod.GET, entity, Transfer[].class);
        return response.getBody();
    }

    public Transfer[] getMyPendingTransfers(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Transfer[]> response =
                restTemplate.exchange(BASE_URL + "transfers/pending", HttpMethod.GET, entity, Transfer[].class);
        return response.getBody();
    }
        

    public Transfer sendTransfer (Transfer transfer, String token){
        try {
            restTemplate.exchange(BASE_URL + "transfers/send", HttpMethod.POST, makeTransferEntity(transfer, token), Transfer.class);
        } catch (RestClientResponseException ex) {
            ex.printStackTrace();
        }
        return transfer;
    }

    public Transfer requestTransfer (Transfer transfer, String token){
        try {
            restTemplate.exchange(BASE_URL + "transfers/request", HttpMethod.POST, makeTransferEntity(transfer, token), Transfer.class);
        } catch (RestClientResponseException ex) {
            ex.printStackTrace();
        }
        return transfer;
    }

    // TODO: Implement approve transfer as a PUT method (controller side)
    public Transfer approveTransfer (Long transferId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + "transfers/approve", HttpMethod.PUT, entity, Transfer.class);
        return response.getBody();


    }

    // TODO: Implement reject transfer as a PUT method (controller side)
    public Transfer rejectTransfer (String token,Long transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + "transfers/reject", HttpMethod.PUT, entity, Transfer.class);
        return response.getBody();


    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }
}