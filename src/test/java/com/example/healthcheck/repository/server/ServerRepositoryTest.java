package com.example.healthcheck.repository.server;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.steps.ServerSteps;

@DataJpaTest
@Import(JpaConfig.class)
class ServerRepositoryTest {

	@Autowired
	private ServerRepository serverRepository;

	private ServerSteps serverSteps;

	@BeforeEach
	void setup() {
		serverSteps = new ServerSteps(serverRepository);
	}

	@Test
	@DisplayName("활성화된 서버만 조회하기")
	void findActiveServerTest() {
		// given
		final Server server = serverSteps.create("노말 서버", true);
		final Server enableServer = serverSteps.create("활성화된 서버", true);
		final Server disableServer = serverSteps.create("비활성화 서버", false);

		// when
		final List<Server> response = serverRepository.findActiveServer();

		// then
		assertThat(response).hasSize(2);
		assertThat(response
			.stream()
			.map(Server::getServerName)
			.collect(toList()))
			.contains(server.getServerName(), enableServer.getServerName());
	}
}
