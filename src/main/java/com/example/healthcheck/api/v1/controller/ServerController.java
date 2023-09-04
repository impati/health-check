package com.example.healthcheck.api.v1.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.healthcheck.api.v1.request.ServerChangeStatusV1Request;
import com.example.healthcheck.api.v1.request.ServerRegistrationV1Request;
import com.example.healthcheck.api.v1.response.Response;
import com.example.healthcheck.security.Customer;
import com.example.healthcheck.service.server.ServerRegister;
import com.example.healthcheck.service.server.ServerStatusManager;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/server")
@RequiredArgsConstructor
public class ServerController {

	private final ServerRegister serverRegister;
	private final ServerStatusManager serverStatusManager;

	@PostMapping
	public Response<Void> register(@RequestBody final ServerRegistrationV1Request request, final Customer customer) {
		serverRegister.register(customer.getEmail(), request.convert());

		return Response.success();
	}

	@PostMapping("/disable")
	public Response<Void> disable(@RequestBody final ServerChangeStatusV1Request request, final Customer customer) {
		serverStatusManager.deactivate(request.convert(), customer.getEmail());

		return Response.success();
	}

	@PostMapping("/enable")
	public Response<Void> enable(@RequestBody final ServerChangeStatusV1Request request, final Customer customer) {
		serverStatusManager.activate(request.convert(), customer.getEmail());

		return Response.success();
	}
}
