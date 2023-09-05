package com.example.healthcheck.service.health;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.healthcheck.entity.health.ActiveServer;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.exception.ErrorCode;
import com.example.healthcheck.exception.HealthCheckException;
import com.example.healthcheck.repository.health.ActiveServerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActiveTableManager {

	private final ActiveServerRepository activeServerRepository;

	public List<ActiveServer> findTargetServer(final long time) {
		return activeServerRepository.findActiveServerByTargetTime(time);
	}

	@Transactional
	public void updateTargetTime(final Server server) {
		final ActiveServer activeServer = findActiveServer(server);
		activeServer.updateTargetTime(server.getInterval());
	}

	@Transactional
	public void delete(final Server server) {
		activeServerRepository.delete(findActiveServer(server));
	}

	@Transactional
	public void insert(final Server server, final long time) {
		if (!server.isActive()) {
			return;
		}

		activeServerRepository.save(ActiveServer.builder()
			.server(server)
			.targetTime(time + server.getInterval())
			.build());
	}

	private ActiveServer findActiveServer(final Server server) {
		return activeServerRepository.findActiveServerByServer(server)
			.orElseThrow(() -> new HealthCheckException(ErrorCode.NOT_ACTIVE_SERVER));
	}
}
