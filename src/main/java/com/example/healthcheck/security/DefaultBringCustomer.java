package com.example.healthcheck.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultBringCustomer implements BringCustomer{

    private static final String CUSTOMER_ENDPOINT = "/api/v1/customer";
    private static final String CLIENT_ID = "clientId";
    private final RestTemplate restTemplate;
    @Value("${customer-server.client-id}")
    private String clientId;
    @Value("${customer-server.customer-server}")
    private String customerServer;

    @Override
    public Customer bring(String token) {
        return restTemplate.exchange(path(), HttpMethod.POST, createRequestHeader(token), Customer.class).getBody();
    }

    protected String client(){
        return clientId;
    }

    protected String path(){
        return customerServer + CUSTOMER_ENDPOINT;
    }

    private HttpEntity createRequestHeader(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(CLIENT_ID, client());
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return new HttpEntity<>(httpHeaders);
    }

}
