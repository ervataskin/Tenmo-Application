package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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

}