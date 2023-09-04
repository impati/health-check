package com.example.healthcheck.repository.health;

import static java.time.LocalDateTime.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.steps.HealthRecordSteps;
import com.example.healthcheck.steps.ServerSteps;

@DataJpaTest
@Import(JpaConfig.class)
class HealthRecordRepositoryTest {

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
	@DisplayName("서버에 대해 가장 최근에 실시한 헬스 체크 가져오기")
	void findTopByServerOrderByCreatedAtDescTest() throws Exception {
		// given
		final Server server = serverSteps.createDefault();

		final HealthRecord firstRecord = healthRecordSteps.create(server);
		Thread.sleep(100);
		final HealthRecord midRecord = healthRecordSteps.create(server);
		Thread.sleep(100);
		final HealthRecord lastRecord = healthRecordSteps.create(server);

		// when
		final Optional<HealthRecord> response = healthRecordRepository.findTopByServerOrderByCreatedAtDesc(server);

		// then
		assertThat(response).isPresent();
		assertThat(response.get().getId()).isEqualTo(lastRecord.getId());
	}

	@Test
	@DisplayName("여러 서버 중 특정 서버 대해 가장 최근에 실시한 헬스 체크 가져오기")
	void findTopByServerOrderByCreatedAtDescWithManyServer() {
		// given
		final Server server = serverSteps.createDefault();
		final Server other = serverSteps.createDefault();
		healthRecordSteps.create(server);
		HealthRecord lastRecord = healthRecordSteps.create(server);
		healthRecordSteps.create(other);

		// when
		final Optional<HealthRecord> response = healthRecordRepository.findTopByServerOrderByCreatedAtDesc(server);

		// then
		assertThat(response).isPresent();
		assertThat(response.get().getId()).isEqualTo(lastRecord.getId());
	}

	@Test
	@DisplayName("활성화된 서버의 최신 Health 레코드 가져오기")
	void findDistinctHealthRecordByActiveServiceTest() {
		// given
		final Server firstTestServer = serverSteps.create("[테스트 서버 1]", true);
		final Server secondTestServer = serverSteps.create("[테스트 서버 2]", true);
		final Server thirdTestServer = serverSteps.create("[테스트 서버 3]", true);

		final HealthRecord firstRecordOfFirstTestServer = healthRecordSteps.create(firstTestServer);
		final HealthRecord lastRecordOfFirstTestServer = healthRecordSteps.create(firstTestServer);

		final HealthRecord firstRecordOfSecondTestServer = healthRecordSteps.create(secondTestServer);
		final HealthRecord lastRecordOfSecondTestServer = healthRecordSteps.create(secondTestServer);

		final List<Server> activeServer = List.of(firstTestServer, secondTestServer, thirdTestServer);

		final LocalDateTime afterTime = now().minusDays(1);

		// when
		final List<HealthRecord> result = healthRecordRepository.findLatestRecordOfActiveServer(
			activeServer,
			afterTime
		);

		// then
		assertThat(result).hasSize(2);
		assertThat(result).containsAnyOf(
			lastRecordOfFirstTestServer, lastRecordOfSecondTestServer
		);
	}
}
