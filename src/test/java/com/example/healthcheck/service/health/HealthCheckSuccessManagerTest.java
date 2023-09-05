package com.example.healthcheck.service.health;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.healthcheck.entity.health.ActiveServer;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.health.ActiveServerRepository;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.steps.ServerSteps;

@SpringBootTest
@Transactional
class HealthCheckSuccessManagerTest {

	@Autowired
	private HealthCheckSuccessManager healthCheckSuccessManager;

	@Autowired
	private ServerRepository serverRepository;

	@Autowired
	private HealthRecordRepository healthRecordRepository;

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
	@DisplayName("헬스 체크 성공시 PROCESS")
	void healthCheckSuccessManager_processTest() {
		// given
		final Server server = serverSteps.createDefault();
		activeServerRepository.save(ActiveServer.builder()
			.server(server)
			.targetTime(30L)
			.build());

		// when
		healthCheckSuccessManager.process(server);

		// then
		assertThat(healthRecordRepository.findAll()).hasSize(1);
	}
}
