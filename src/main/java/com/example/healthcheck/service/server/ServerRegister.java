package com.example.healthcheck.service.server;

import static com.example.healthcheck.util.TimeConverter.*;
import static java.time.LocalDateTime.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.service.health.ActiveTableManager;
import com.example.healthcheck.service.server.dto.ServerRegistrationDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ServerRegister {

	private final ServerRepository serverRepository;
	private final ActiveTableManager activeTableManager;

	public void register(final String email, final ServerRegistrationDto registrationDto) {
		final Server server = serverRepository.save(toServer(email, registrationDto));
		activeTableManager.insert(server, convertToLong(now()));
	}

	private Server toServer(final String email, final ServerRegistrationDto dto) {
		return Server.builder()
			.serverName(dto.serverName())
			.email(email)
			.method(dto.method())
			.interval(dto.interval())
			.host(dto.host())
			.path(dto.path())
			.active(dto.active())
			.params(dto.queryParams())
			.build();
	}
}
