package com.example.healthcheck.service.health;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.service.health.dto.HealthCheckServer;
import com.example.healthcheck.steps.HealthRecordSteps;
import com.example.healthcheck.steps.ServerSteps;

@DataJpaTest(showSql = false)
@Import({JpaConfig.class, DefaultHealthTargetImporter.class})
class DefaultHealthTargetImporterTest {

	@Autowired
	private HealthTargetImporter healthTargetImporter;

	@Autowired
	private HealthRecordRepository healthRecordRepository;

	@Autowired
	private ServerRepository serverRepository;

	private HealthRecordSteps healthRecordSteps;

	private ServerSteps serverSteps;

	@BeforeEach
	void setup() {
		healthRecordSteps = new HealthRecordSteps(healthRecordRepository);
		serverSteps = new ServerSteps(serverRepository);
	}

	@Test
	@DisplayName("헬스 체크 대상 서버를 HealthCheckServer 객체로 가져오기 테스트")
	void importTargetTest() {
		// given
		final Server firstTestServer = serverSteps.create("[테스트 서버 1]", true, 3);
		final Server secondTestServer = serverSteps.create("[테스트 서버 2]", true, 10);
		final Server thirdTestServer = serverSteps.create("[테스트 서버 3]", true, 30);

		final HealthRecord firstRecordOfFirstTestServer = healthRecordSteps.create(firstTestServer);
		final HealthRecord lastRecordOfFirstTestServer = healthRecordSteps.create(firstTestServer);

		final HealthRecord firstRecordOfSecondTestServer = healthRecordSteps.create(secondTestServer);
		final HealthRecord lastRecordOfSecondTestServer = healthRecordSteps.create(secondTestServer);

		// when
		final List<HealthCheckServer> healthCheckServers = healthTargetImporter.importTarget(10);

		// then
		assertThat(healthCheckServers).hasSize(3);
	}
}
