package com.example.healthcheck.api.v1.controller;

import com.example.healthcheck.api.v1.request.ServerDisableV1Request;
import com.example.healthcheck.api.v1.request.ServerRegistrationV1Request;
import com.example.healthcheck.api.v1.response.Response;
import com.example.healthcheck.security.Customer;
import com.example.healthcheck.service.server.ServerDeActivator;
import com.example.healthcheck.service.server.ServerRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/server")
@RequiredArgsConstructor
public class ServerController {
    private final ServerRegister serverRegister;
    private final ServerDeActivator serverDeActivator;
    @PostMapping
    public Response<Void> register(@RequestBody ServerRegistrationV1Request request, Customer customer){
        serverRegister.register(customer.getEmail(),request.convert());
        return Response.success();
    }

    @PostMapping("/disable")
    public Response<Void> disable(@RequestBody ServerDisableV1Request request, Customer customer){
        serverDeActivator.deactivate(customer.getEmail(),request.convert());
        return Response.success();
    }
}
