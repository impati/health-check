package com.example.healthcheck.service.health;

import static com.example.healthcheck.util.TimeConverter.*;
import static java.time.LocalDateTime.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

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
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.steps.ServerSteps;

@SpringBootTest
@Transactional
class ActiveTableManagerTest {

	@Autowired
	private ActiveTableManager activeTableManager;

	@Autowired
	private ActiveServerRepository activeServerRepository;

	@Autowired
	private ServerRepository serverRepository;

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
	@DisplayName("ActiveServer 삽입 테스트")
	void insertTest() {
		// given
		final Server activeServer = serverSteps.create("[테스트 서버 1]", true);
		final Server inActiveServer = serverSteps.create("[테스트 서버 2]", false);

		// when
		activeTableManager.insert(activeServer, convertToLong(now()));
		activeTableManager.insert(inActiveServer, convertToLong(now()));

		// then
		final List<ActiveServer> response = activeServerRepository.findAll();
		assertThat(response).hasSize(1);
		assertThat(response.stream().findFirst().get().getServer()).isEqualTo(activeServer);
	}

	@Test
	@DisplayName("targetTime 업데이트 테스트")
	void updateTargetTimeTest() {
		// given
		final Server activeServer = serverSteps.create("[테스트 서버 1]", true);
		final long currentTime = convertToLong(now());
		activeTableManager.insert(activeServer, currentTime);

		// when
		activeTableManager.updateTargetTime(activeServer);

		// then
		final long targetTime = currentTime + activeServer.getInterval() * 2;
		final List<ActiveServer> response = activeTableManager.findTargetServer(targetTime);
		assertThat(response).hasSize(1);
		assertThat(response.get(0).getServer()).isEqualTo(activeServer);
	}

	@Test
	@DisplayName("ActiveServer Table 에서 제거")
	void deleteTest() {
		// given
		final Server activeServer = serverSteps.create("[테스트 서버 1]", true);
		final long currentTime = convertToLong(now());
		activeTableManager.insert(activeServer, currentTime);

		// when
		activeTableManager.delete(activeServer);

		// then
		final List<ActiveServer> response = activeServerRepository.findAll();
		assertThat(response).isEmpty();
	}
}
