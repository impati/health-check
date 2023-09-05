package com.example.healthcheck.service.health;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.exception.HealthCheckException;
import com.example.healthcheck.repository.health.ActiveServerRepository;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.steps.ServerSteps;

@SpringBootTest
@Transactional
class DefaultHealthCheckerTest {

	@Autowired
	private HealthChecker healthChecker;

	@Autowired
	private ServerRepository serverRepository;

	@Autowired
	private ActiveServerRepository activeServerRepository;

	private ServerSteps serverSteps;

	@BeforeEach
	void setup() {
		serverSteps = new ServerSteps(serverRepository);
	}

	@AfterEach
	void tearDown() {
		activeServerRepository.deleteAll();
		serverRepository.deleteAll();
	}

	@Test
	@DisplayName("헬스 체킹 실패 테스트")
	void HealthChecker_check_SUCCESS() {
		// given
		final Server server = serverSteps.createNonexistentServer();

		// expected
		assertThatCode(() -> healthChecker.check(server)).isInstanceOf(HealthCheckException.class);
	}

	@Test
	@DisplayName("헬스 체킹 성공 테스트")
	void HealthChecker_check_FAIL() {
		// given
		final Server server = serverSteps.createExistServer();

		// expected
		assertThatCode(() -> healthChecker.check(server)).doesNotThrowAnyException();
	}
}
