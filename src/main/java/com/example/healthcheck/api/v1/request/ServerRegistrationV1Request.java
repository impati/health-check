package com.example.healthcheck.api.v1.request;


import com.example.healthcheck.entity.server.EndPointHttpMethod;
import com.example.healthcheck.service.server.dto.ServerRegistrationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerRegistrationV1Request{
    private String host;
    private String path;
    private EndPointHttpMethod method;
    private Integer interval;
    private boolean active;

    public ServerRegistrationDto convert(){
        return ServerRegistrationDto.builder()
                .active(active)
                .host(host)
                .path(path)
                .interval(interval)
                .method(method)
                .build();
    }
}
