package com.example.healthcheck.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.healthcheck.service.health.ActiveTableHealthTargetChecker;
import com.example.healthcheck.service.health.ActiveTableHealthTargetImporter;
import com.example.healthcheck.service.health.ActiveTableManager;
import com.example.healthcheck.service.health.AsyncInnerHealthCheckRequester;
import com.example.healthcheck.service.health.HealthCheckManager;
import com.example.healthcheck.service.health.HealthCheckRequester;
import com.example.healthcheck.service.health.HealthTargetChecker;
import com.example.healthcheck.service.health.HealthTargetImporter;
import com.example.healthcheck.service.health.HealthTimeChecker;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class HealthConfig {

	@Bean
	public HealthTimeChecker healthTimeChecker(final HealthTargetChecker healthTargetChecker) {
		return new HealthTimeChecker(healthTargetChecker);
	}

	@Bean
	public HealthTargetChecker healthTargetChecker(
		final HealthCheckRequester healthCheckRequester,
		final HealthTargetImporter targetImporter
	) {
		return new ActiveTableHealthTargetChecker(targetImporter, healthCheckRequester);
	}

	@Bean
	public HealthCheckRequester healthCheckRequester(final HealthCheckManager healthCheckManager) {
		return new AsyncInnerHealthCheckRequester(healthCheckManager);
	}

	@Bean
	public HealthTargetImporter healthTargetImporter(final ActiveTableManager activeTableManager) {
		return new ActiveTableHealthTargetImporter(activeTableManager);
	}
}
