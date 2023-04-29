package com.example.healthcheck.steps;

import com.example.healthcheck.entity.server.EndPointHttpMethod;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.server.ServerRepository;
import org.springframework.util.LinkedMultiValueMap;

public class ServerSteps {

    private final ServerRepository serverRepository;

    public ServerSteps(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public Server createDefault(){
        return serverRepository.save(Server.builder()
                .customerId(1L)
                .method(EndPointHttpMethod.GET)
                .interval(30)
                .host("http://localhost:8080")
                .active(true)
                .params(new LinkedMultiValueMap<>())
                .build());
    }

    public Server createNonexistentServer(){
        return serverRepository.save(Server.builder()
                .customerId(1L)
                .method(EndPointHttpMethod.GET)
                .interval(30)
                .host("http://XXXX:8080")
                .active(true)
                .params(new LinkedMultiValueMap<>())
                .build());
    }

    public Server createExistServer(){
        return serverRepository.save(Server.builder()
                .customerId(1L)
                .method(EndPointHttpMethod.GET)
                .interval(30)
                .host("https://naver.com")
                .active(true)
                .params(new LinkedMultiValueMap<>())
                .build());
    }

}
