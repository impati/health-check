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

    public Server createServer(String host){
        return serverRepository.save(Server.builder()
                .serverName("호스트를 지정한 서버")
                .customerId(1L)
                .method(EndPointHttpMethod.GET)
                .interval(30)
                .host(host)
                .active(true)
                .params(new LinkedMultiValueMap<>())
                .build());
    }

    public Server createDefault(){
        return serverRepository.save(Server.builder()
                .serverName("디폴트 서버")
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
                .serverName("존재하지 않는 서버")
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
                .serverName("존재하는 서버")
                .customerId(1L)
                .method(EndPointHttpMethod.GET)
                .interval(30)
                .host("https://naver.com")
                .active(true)
                .params(new LinkedMultiValueMap<>())
                .build());
    }

}
