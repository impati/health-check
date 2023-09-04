package com.example.healthcheck.service.health;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.config.RetryConfig;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.exception.HealthCheckException;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.steps.ServerSteps;

@DataJpaTest
@Import({
	DefaultHealthChecker.class,
	RestTemplate.class,
	JpaConfig.class,
	RetryConfig.class
})
class DefaultHealthCheckerTest {

	@Autowired
	private HealthChecker healthChecker;

	@Autowired
	private ServerRepository serverRepository;

	private ServerSteps serverSteps;

	@BeforeEach
	void setup() {
		serverSteps = new ServerSteps(serverRepository);
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
