package com.example.healthcheck.service.server.dto;

import com.example.healthcheck.entity.server.EndPointHttpMethod;
import lombok.Builder;

@Builder
public record ServerRegistrationDto (
        String host,
        String path,
        EndPointHttpMethod method,
        Integer interval,
        boolean active
){
}
