package com.example.healthcheck.service.health.dto;

import static org.assertj.core.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HealthCheckServerTest {

	@Test
	@DisplayName("checkTime 테스트")
	void checkTimeTest() {
		final LocalDateTime now = LocalDateTime.now();
		final LocalDateTime after30Minute = now.plusMinutes(30);
		assertThat(now.compareTo(after30Minute)).isNegative();
		final Duration between = Duration.between(now, after30Minute);
		assertThat(between.getSeconds() / 60).isEqualTo(30);
	}
}
