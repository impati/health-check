package com.example.healthcheck.service.health;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SynchronousInnerHealCheckRequester implements HealthCheckRequester {

	private final HealthCheckManager healthCheckManager;

	@Override
	public void check(final Long serverId) {
		healthCheckManager.check(serverId);
	}
}
