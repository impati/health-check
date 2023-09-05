package com.example.healthcheck.service.health;

import java.util.List;
import java.util.stream.Collectors;

import com.example.healthcheck.service.health.dto.HealthCheckServer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ActiveTableHealthTargetImporter implements HealthTargetImporter {

	private final ActiveTableManager activeTableManager;

	@Override
	public List<HealthCheckServer> importTarget(final long time) {
		return activeTableManager.findTargetServer(time)
			.stream()
			.map(activeServer -> HealthCheckServer.of(activeServer.getServer(), activeServer.getTargetTime()))
			.collect(Collectors.toList());
	}
}
