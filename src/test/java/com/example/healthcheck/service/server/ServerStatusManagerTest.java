package com.example.healthcheck.service.server;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
import com.example.healthcheck.service.health.ActiveTableManager;
import com.example.healthcheck.service.server.dto.ServerDto;
import com.example.healthcheck.steps.ServerSteps;

@SpringBootTest
@Transactional
class ServerStatusManagerTest {

	@Autowired
	private ServerStatusManager serverStatusManager;

	@Autowired
	private ServerRepository serverRepository;

	@Autowired
	private ActiveTableManager activeTableManager;

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
	@DisplayName("서버 비활성화 테스트 - 내부")
	void deactivateTestInner() {
		// given
		final Server server = serverSteps.createDefault();
		activeTableManager.insert(server, 30);

		// when
		serverStatusManager.deactivate(server);

		// then
		assertThat(server.isActive()).isFalse();
	}

	@Test
	@DisplayName("서버 비활성화 테스트 - 외부 ,정상")
	void deactivateTestExternal() {
		// given
		final Server server = serverSteps.createDefault();
		activeTableManager.insert(server, 30);
		final ServerDto serverDto = ServerDto.builder()
			.serverName(server.getServerName())
			.serverId(server.getId())
			.build();

		// when
		serverStatusManager.deactivate(serverDto, server.getEmail());

		// then
		assertThat(serverRepository.findById(server.getId()).get().isActive()).isFalse();
	}

	@Test
	@DisplayName("서버 비활성화 테스트 - 외부 ,서버 이름 다른 경우")
	void deactivateTestExternalFailDifferent() {
		// given
		final Server server = serverSteps.createDefault();
		activeTableManager.insert(server, 30);
		final ServerDto serverDto = ServerDto.builder()
			.serverName("다른 서버")
			.serverId(server.getId())
			.build();

		// expected
		final HealthCheckException healthCheckException = assertThrows(HealthCheckException.class,
			() -> serverStatusManager.deactivate(serverDto, server.getEmail()));
		assertThat(healthCheckException.getMessage())
			.isEqualTo("입력한 서버 정보가 올바르지 않습니다.");
	}

	@Test
	@DisplayName("서버 비활성화 테스트 - 외부 서버 ,사용자가 아닌 경우")
	void deactivateTestExternalFailOtherCustomer() {
		// given
		final Server server = serverSteps.createDefault();
		activeTableManager.insert(server, 30);
		final ServerDto serverDto = ServerDto.builder()
			.serverName(server.getServerName())
			.serverId(server.getId())
			.build();

		// expected
		HealthCheckException healthCheckException = assertThrows(HealthCheckException.class,
			() -> serverStatusManager.deactivate(serverDto, server.getEmail() + "noisy"));

		assertThat(healthCheckException.getMessage()).isEqualTo("다른 사용자 영역에 침범했습니다.");
	}

	@Test
	@DisplayName("서버 활성화 테스트")
	void deactivateTest() {
		// given
		final Server server = serverSteps.create("테스트 서버", false);
		final ServerDto serverDto = ServerDto.builder()
			.serverName(server.getServerName())
			.serverId(server.getId())
			.build();

		// when
		serverStatusManager.activate(serverDto, server.getEmail());

		// then
		assertThat(serverRepository.findById(server.getId()).get().isActive()).isTrue();
	}
}
