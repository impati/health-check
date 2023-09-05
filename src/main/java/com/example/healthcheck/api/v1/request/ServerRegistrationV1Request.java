package com.example.healthcheck.api.v1.request;

import java.util.List;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.healthcheck.entity.server.EndPointHttpMethod;
import com.example.healthcheck.service.server.dto.ServerRegistrationDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerRegistrationV1Request {

    private String serverName;
    private String host;
    private String path;
    private EndPointHttpMethod method;
    private List<QueryParamRequest> queryParams;
    private Integer interval;
    private boolean active;

    public ServerRegistrationDto convert() {
        return ServerRegistrationDto.builder()
            .serverName(serverName)
            .host(host)
            .path(path)
            .method(method)
            .queryParams(toMultiValueMap())
            .interval(interval)
            .active(active)
            .build();
    }

    private MultiValueMap<String, String> toMultiValueMap() {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        for (var element : queryParams) {
            params.add(element.getKey(), element.getValue());
        }

        return params;
    }
}
