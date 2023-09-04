package com.example.healthcheck.service.health;

import org.springframework.scheduling.annotation.Async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AsyncInnerHealthCheckRequester implements HealthCheckRequester {

	private final HealthCheckManager healthCheckManager;

	@Async("healthCheckRequesterTaskExecutor")
	@Override
	public void check(final Long serverId) {
		healthCheckManager.check(serverId);
	}
}
