package com.example.healthcheck.entity.server;

import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
public enum EndPointHttpMethod {
    GET;

    public HttpMethod convertHttpMethod(){
        if(this == GET) return HttpMethod.GET;
        return null;
    }
}
