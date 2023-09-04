package com.example.healthcheck.service.health;

import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.exception.ErrorCode;
import com.example.healthcheck.exception.HealthCheckException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultHealthChecker implements HealthChecker {

	private final RestTemplate restTemplate;

	@Retryable(
		retryFor = HealthCheckException.class,
		maxAttempts = 3
	)
	public void check(final Server server) {
		try {
			restTemplate.getForObject(server.getUrl(), String.class);
		} catch (RestClientException e) {
			throw new HealthCheckException(ErrorCode.HEALTH_CHECK_FAIL);
		}
	}
}
