package com.example.healthcheck.service.server;

import static com.example.healthcheck.util.TimeConverter.*;
import static java.time.LocalDateTime.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.exception.ErrorCode;
import com.example.healthcheck.exception.HealthCheckException;
import com.example.healthcheck.service.common.EntityFinder;
import com.example.healthcheck.service.health.ActiveTableManager;
import com.example.healthcheck.service.server.dto.ServerDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerStatusManager {

	private final EntityFinder entityFinder;
	private final ActiveTableManager activeTableManager;

	@Transactional
	public void deactivate(final Server server) {
		server.deactivate();
		activeTableManager.delete(server);
	}

	@Transactional
	public void deactivate(final ServerDto serverDto, final String email) {
		final Server server = getServer(serverDto.serverId(), serverDto.serverName(), email);
		server.deactivate();
	}

	@Transactional
	public void activate(final ServerDto serverDto, final String email) {
		final Server server = getServer(serverDto.serverId(), serverDto.serverName(), email);
		server.activate();
		activeTableManager.insert(server, convertToLong(now()));
	}

	private Server getServer(final Long serverId, final String serverName, final String email) {
		final Server server = entityFinder.findOrElseThrow(serverId, Server.class);
		serverValidate(server, serverName, email);
		return server;
	}

	private void serverValidate(final Server server, final String serverName, final String email) {
		if (!isSameServer(server.getServerName(), serverName)) {
			throw new HealthCheckException(ErrorCode.SERVER_MISMATCH);
		}
		if (!isOwner(server, email)) {
			throw new HealthCheckException(ErrorCode.INVALID_ACCESS);
		}
	}

	private boolean isSameServer(final String realServerName, final String requestServerName) {
		return realServerName.equals(requestServerName);
	}

	private boolean isOwner(final Server server, final String email) {
		return server.getEmail().equals(email);
	}
}
